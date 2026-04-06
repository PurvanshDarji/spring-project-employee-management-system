let dataTable = null;
let editId = null;

document.addEventListener("DOMContentLoaded", function () {
    loadEmployees();
});


// ================= LOAD FROM DATABASE =================
function loadEmployees() {

    fetch("http://localhost:8080/api/employee/all")
        .then(res => res.json())
        .then(data => {

            empTable.innerHTML = "";

            data.forEach(e => {
                empTable.innerHTML += `
                    <tr>
                        <td>${e.id}</td>
                        <td>${e.name}</td>
                        <td>${e.department}</td>
                        <td>${e.designation}</td>
                        <td>${e.contact}</td>
                        <td>
                            <button class="action-btn" onclick="editEmp(${e.id})">Edit</button>
                            <button class="action-btn delete" onclick="deleteEmp(${e.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });

            initializeDataTable();
        });
}


// ================= DATATABLE INIT =================
function initializeDataTable() {

    if ($.fn.DataTable.isDataTable('#employeeTable')) {
        $('#employeeTable').DataTable().destroy();
    }

    dataTable = new DataTable("#employeeTable", {
        pageLength: 5
    });
}


// ================= OPEN / CLOSE MODAL =================
function openModal() {
    modal.style.display = "flex";
}

function closeModal() {
    modal.style.display = "none";
    clearForm();
}


// ================= SAVE / UPDATE =================
function saveEmployee() {

    const employee = {
        name: name.value.trim(),
        department: dept.value.trim(),
        designation: desig.value.trim(),
        contact: phone.value.trim()
    };

    if (!employee.name || !employee.department || !employee.designation || !employee.contact) {
        showToast("All fields required!", true);
        return;
    }

    // 🔹 ADD
    if (editId === null) {

        fetch("http://localhost:8080/api/employee/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(employee)
        })
        .then(res => res.json())
        .then(() => {
            showToast("Employee Added");
            closeModal();
            loadEmployees();
        });

    }
    // 🔹 UPDATE
    else {

        fetch(`http://localhost:8080/api/employee/update/${editId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(employee)
        })
        .then(res => res.text())
        .then(() => {
            showToast("Employee Updated");
            editId = null;
            formTitle.innerText = "Add Employee";
            closeModal();
            loadEmployees();
        });
    }
}


// ================= EDIT =================
function editEmp(id) {

    fetch(`http://localhost:8080/api/employee/${id}`)
        .then(res => res.json())
        .then(e => {

            name.value = e.name;
            dept.value = e.department;
            desig.value = e.designation;
            phone.value = e.contact;

            editId = id;
            formTitle.innerText = "Edit Employee";
            openModal();
        });
}


// ================= DELETE =================
function deleteEmp(id) {

    if (confirm("Delete this employee?")) {

        fetch(`http://localhost:8080/api/employee/${id}`, {
            method: "DELETE"
        })
        .then(res => res.text())
        .then(() => {
            showToast("Employee Deleted");
            loadEmployees();
        });
    }
}


// ================= CLEAR FORM =================
function clearForm() {
    name.value = "";
    dept.value = "";
    desig.value = "";
    phone.value = "";
}


// ================= TOAST =================
function showToast(msg, error = false) {

    toast.innerText = msg;
    toast.style.background = error ? "#ef4444" : "#22c55e";
    toast.style.display = "block";

    setTimeout(() => toast.style.display = "none", 2500);
}