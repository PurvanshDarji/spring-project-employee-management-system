const API = "http://localhost:8080/api/employee";

// Load employees when page loads
window.onload = loadEmployees;

function saveEmployee() {

    let emp = {
        name: document.getElementById("name").value.trim(),
        department: document.getElementById("dept").value.trim(),
        designation: document.getElementById("desig").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        password: document.getElementById("password").value.trim()
    };

    if (Object.values(emp).some(v => v === "")) {
        showToast("Please fill all fields");
        return;
    }

    fetch(API + "/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(emp)
    })
    .then(res => res.json())
    .then(() => {
        showToast("Employee saved successfully");
        clearForm();
        loadEmployees();   // reload data from DB
    });
}

function loadEmployees() {

    fetch(API + "/all")
        .then(res => res.json())
        .then(data => {

            let table = document.getElementById("empTable");
            table.innerHTML = "";

            data.forEach(emp => {
                table.innerHTML += `
                    <tr>
                        <td>${emp.id}</td>
                        <td>${emp.name}</td>
                        <td>${emp.department}</td>
                        <td>${emp.designation}</td>
                        <td>${emp.phone}</td>
                        <td>
                            <button onclick="deleteEmployee(${emp.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });
        });
}

function deleteEmployee(id) {

    fetch(API + "/" + id, { method: "DELETE" })
        .then(() => {
            showToast("Employee deleted");
            loadEmployees();
        });
}

function clearForm() {
    ["name", "dept", "desig", "phone", "password"].forEach(id => {
        document.getElementById(id).value = "";
    });
}

function showToast(msg) {
    let toast = document.getElementById("toast");
    toast.innerText = msg;
    toast.classList.add("show");

    setTimeout(() => toast.classList.remove("show"), 2500);
}
