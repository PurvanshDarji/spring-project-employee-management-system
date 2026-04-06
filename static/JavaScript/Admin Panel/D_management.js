const api = "http://localhost:8082/api/department";  // ✅ Update this to your actual API endpoint
const dname = document.getElementById("dname");
const dhead = document.getElementById("dhead");
const ddesc = document.getElementById("ddesc");
const deptTable = document.getElementById("deptTable");
const toast = document.getElementById("toast");
const formTitle = document.getElementById("formTitle");
const editId = document.getElementById("editId");
const cancelBtn = document.getElementById("cancelBtn");

/* Load Employees for Department Head Dropdown */
function loadEmployeesForHead(){
    fetch(api + "/employees")
    .then(res => res.json())
    .then(data => {
        dhead.innerHTML = `<option value="">Select Department Head</option>`;
        data.forEach(e => {
            dhead.innerHTML += `<option value="${e.id}|${e.name}">${e.name}</option>`;
        });
    })
    .catch(err => showToast("Error loading employees: " + err.message, true));
}

/* ✅ NEW: Load Single Department for Edit */
function loadDepartment(id) {
    fetch(api + "/" + id)
    .then(res => res.json())
    .then(dept => {
        editId.value = dept.id;
        dname.value = dept.name;
        ddesc.value = dept.description || "";
        
        // Set head dropdown
        const headOption = Array.from(dhead.options).find(opt => 
            opt.value.includes(dept.headId + "|")
        );
        if (headOption) {
            dhead.value = headOption.value;
        }
        
        // Update UI for edit mode
        formTitle.textContent = "Edit Department";
        cancelBtn.style.display = "inline-block";
        dname.focus();
    })
    .catch(err => showToast("Error loading department: " + err.message, true));
}

/* Save/Update Department */
function saveDepartment(){
    if(!dname.value.trim() || !dhead.value){
        showToast("All fields required!", true);
        return;
    }

    const headData = dhead.value.split("|");
    const dept = {
        id: editId.value ? parseInt(editId.value) : null,
        name: dname.value.trim(),
        headId: parseInt(headData[0]),
        headName: headData[1],
        description: ddesc.value.trim()
    };

    const url = editId.value ? api + "/update" : api + "/save";
    
    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dept)
    })
    .then(res => res.json())
    .then(() => {
        showToast(editId.value ? "Department Updated Successfully!" : "Department Saved Successfully!");
        clearForm();
        loadDepartments();
    })
    .catch(err => showToast("Error saving department: " + err.message, true));
}

/* ✅ NEW: Edit Department */
function editDept(id) {
    editId.value = id;
    loadDepartment(id);
}

/* Load Departments */
function loadDepartments(){
    fetch(api + "/all")
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector("#deptTable");
        tbody.innerHTML = "";
        data.forEach(d => {
            tbody.innerHTML += `
                <tr>
                    <td>${d.id}</td>
                    <td>${d.name}</td>
                    <td>${d.headName}</td>
                    <td>${d.description || ''}</td>
                    <td>
                        <button class="btn btn-primary btn-sm" onclick="editDept(${d.id})">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteDept(${d.id})">Delete</button>
                    </td>
                </tr>
            `;
        });
    })
    .catch(err => showToast("Error loading departments: " + err.message, true));
}

/* Delete Department */
function deleteDept(id){
    if(confirm("Delete this department?")){
        fetch(api + "/" + id, { method:"DELETE" })
        .then(() => {
            showToast("Department Deleted Successfully!");
            loadDepartments();
        })
        .catch(err => showToast("Error deleting department: " + err.message, true));
    }
}

/* Clear Form */
function clearForm(){
    dname.value = "";
    dhead.value = "";
    ddesc.value = "";
    editId.value = "";
    formTitle.textContent = "Add Department";
    cancelBtn.style.display = "none";
}

/* Toast */
function showToast(msg, error=false){
    toast.innerText = msg;
    toast.style.background = error ? "#ef4444" : "#22c55e";
    toast.style.display = "block";
    setTimeout(()=>toast.style.display="none", 3000);
}

/* On Page Load */
window.onload = () => {
    loadEmployeesForHead();
    loadDepartments();
    
    // ✅ Check if editing (from Thymeleaf)
    const urlParams = new URLSearchParams(window.location.search);
    const editIdParam = urlParams.get('id');
    if (editIdParam) {
        editDept(parseInt(editIdParam));
    }
};