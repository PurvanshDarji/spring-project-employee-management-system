const api = "http://localhost:8082/api";  // ✅ Correct base URL

const emp = document.getElementById("emp");
const dept = document.getElementById("dept");
const baseInput = document.getElementById("base");
const allowInput = document.getElementById("allow");
const deductInput = document.getElementById("deduct");
const netSpan = document.getElementById("net");
const salaryTable = document.getElementById("salaryTable");
const toast = document.getElementById("toast");

function loadEmployees(){
    fetch(api + "/salary/employees")  // ✅ Correct
    .then(res => res.json())
    .then(data => {
        emp.innerHTML = `<option value="">Select Employee</option>`;
        data.forEach(e => {
            emp.innerHTML += `<option value="${e.id}">${e.name}</option>`;
        });
    })
    .catch(err => {
        console.error('Employees load error:', err);
        showToast("Error loading employees", true);
    });
}

function calcSalary(){
    const base = parseFloat(baseInput.value) || 0;
    const allow = parseFloat(allowInput.value) || 0;
    const deduct = parseFloat(deductInput.value) || 0;

    const total = (base + allow) - deduct;
    netSpan.innerText = "₹ " + (total > 0 ? total.toFixed(2) : 0);
}

function saveSalary(){
    const empId = emp.value;
    const empName = emp.options[emp.selectedIndex]?.text || '';

    const base = parseFloat(baseInput.value) || 0;
    const allow = parseFloat(allowInput.value) || 0;
    const deduct = parseFloat(deductInput.value) || 0;
    const netSalary = (base + allow) - deduct;

    if(!empId || !dept.value || base <= 0){
        showToast("Fill all required fields", true);
        return;
    }

    const salary = {
        empId: parseInt(empId),  // ✅ Number बनाओ
        empName: empName,
        department: dept.value,
        baseSalary: base,
        allowance: allow,
        deduction: deduct,
        netSalary: netSalary
    };

    // ✅ FIXED: /salary/save endpoint
    fetch(api + "/salary/save", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(salary)
    })
    .then(res => res.text())  // ✅ text() use करो (String response)
    .then(msg => {
        showToast(msg || "Salary Saved");
        clearForm();
        loadSalaries();
    })
    .catch(err => {
        console.error('Save error:', err);
        showToast("Save failed", true);
    });
}

function loadSalaries(){
    // ✅ FIXED: /salary/all endpoint
    fetch(api + "/salary/all")
    .then(res => res.json())
    .then(data => {
        salaryTable.innerHTML = "";
        if(data.length === 0){
            salaryTable.innerHTML = '<tr><td colspan="8">No salary records</td></tr>';
            return;
        }
        data.forEach(s => {
            salaryTable.innerHTML += `
                <tr>
                    <td>${s.id || ''}</td>
                    <td>${s.empName || ''}</td>
                    <td>${s.department || ''}</td>
                    <td>₹${(s.baseSalary || 0).toFixed(2)}</td>
                    <td>₹${(s.allowance || 0).toFixed(2)}</td>
                    <td>₹${(s.deduction || 0).toFixed(2)}</td>
                    <td><b>₹${(s.netSalary || 0).toFixed(2)}</b></td>
                    <td>
                        <button onclick="deleteSalary(${s.id})" style="background:red;color:white;border:none;padding:5px;">❌</button>
                    </td>
                </tr>
            `;
        });
    })
    .catch(err => {
        console.error('Salaries load error:', err);
        salaryTable.innerHTML = '<tr><td colspan="8" style="color:red;">Error loading salaries</td></tr>';
        showToast("Error loading salaries", true);
    });
}

function deleteSalary(id){
    if(confirm("Delete this salary record?")){
        // ✅ FIXED: /salary/{id} endpoint
        fetch(api + "/salary/" + id, { 
            method:"DELETE" 
        })
        .then(res => res.text())
        .then(msg => {
            showToast("Salary Deleted");
            loadSalaries();
        })
        .catch(err => {
            console.error('Delete error:', err);
            showToast("Delete failed", true);
        });
    }
}

function clearForm(){
    emp.value = "";
    dept.value = "";
    baseInput.value = "";
    allowInput.value = "";
    deductInput.value = "";
    netSpan.innerText = "₹ 0";
    calcSalary();  // ✅ Recalculate
}

// ✅ Enhanced Toast
function showToast(msg, error=false){
    toast.innerText = msg;
    toast.style.background = error ? "#ef4444" : "#22c55e";
    toast.style.color = "white";
    toast.style.padding = "10px";
    toast.style.borderRadius = "5px";
    toast.style.display = "block";
    setTimeout(()=> toast.style.display = "none", 2500);
}

// ✅ Event Listeners
baseInput.addEventListener('input', calcSalary);
allowInput.addEventListener('input', calcSalary);
deductInput.addEventListener('input', calcSalary);

// ✅ Page Load with Error Handling
window.onload = () => {
    console.log('🌐 Loading Salary Management...');
    console.log('📡 API Base:', api);
    
    loadEmployees();
    loadSalaries();
};