// 🔥 DATABASE CONNECTED - FINAL CLEAN VERSION
document.addEventListener("DOMContentLoaded", function () {

    const API = "http://localhost:8082/api/feedback";

    const form = document.getElementById("feedbackForm");
    const userDisplay = document.getElementById("userDisplay");
    const submitBtn = document.getElementById("submitBtn");
    const successMsg = document.getElementById("successMsg");
    const errorMsg = document.getElementById("errorMsg");

    const empIdField = document.getElementById("empId");
    const empNameField = document.getElementById("empName");
    const currentDeptField = document.getElementById("currentDept");
    const deptSelect = document.getElementById("departmentId");

    // 🔥 STEP 1: Load User + Departments
    async function loadUserAndDepartments() {
        try {
            // 👤 Load user
            const userRes = await fetch(API + "/user");
            const userData = await userRes.json();

            if (userData.empId) {
                if (empIdField) empIdField.value = userData.empId;
                if (empNameField) empNameField.value = userData.empName;
                if (currentDeptField) currentDeptField.value = userData.department || "General";

                if (userDisplay) {
                    userDisplay.textContent = `${userData.empName} (${userData.department})`;
                }
            }

            // 🏢 Load departments
            const deptRes = await fetch(API + "/departments");
            const departments = await deptRes.json();

            if (deptSelect) {
                deptSelect.innerHTML = '<option value="">Select Department</option>';

                departments.forEach(dept => {
                    const option = document.createElement("option");
                    option.value = dept.id;
                    option.textContent = dept.name;
                    deptSelect.appendChild(option);
                });
            }

            console.log("✅ User + Departments Loaded");

        } catch (error) {
            console.error("❌ Load error:", error);
            showAlert("⚠️ Please login first!", "error");
        }
    }

    // 🔥 STEP 2: Submit Form
    if (form) {
        form.addEventListener("submit", async function (e) {
            e.preventDefault();

            const formData = {
                subject: document.getElementById("subject").value.trim(),
                message: document.getElementById("message").value.trim(),
                departmentId: deptSelect?.value || null
            };

            if (!formData.subject || !formData.message) {
                showAlert("❌ Subject & Message required!", "error");
                return;
            }

            const originalText = submitBtn.textContent;
            submitBtn.innerHTML = "Saving...";
            submitBtn.disabled = true;

            try {
                const response = await fetch(API + "/save", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(formData)
                });

                const result = await response.json();

                if (result.success) {

                    showAlert("✅ Feedback saved! ID: " + result.id, "success");

                    // 🔥 ONLY clear feedback fields (BEST UX)
                    document.getElementById("subject").value = "";
                    document.getElementById("message").value = "";
                    if (deptSelect) deptSelect.value = "";

                    // 🔥 OPTIONAL: reload user again
                    loadUserAndDepartments();

                } else {
                    throw new Error(result.message || "Save failed");
                }

            } catch (error) {
                console.error("❌ Save error:", error);
                showAlert("❌ " + error.message, "error");
            } finally {
                submitBtn.textContent = originalText;
                submitBtn.disabled = false;
            }
        });
    }

    // 🔥 STEP 3: Alert System
    function showAlert(message, type) {
        if (!successMsg || !errorMsg) return;

        successMsg.textContent = message;
        successMsg.className = type === "success" ? "alert-success" : "alert-error";
        successMsg.style.display = "block";

        errorMsg.style.display = "none";

        if (type === "success") {
            setTimeout(() => {
                successMsg.style.display = "none";
            }, 3000);
        }
    }

    // 🔥 STEP 4: Character Counter
    const messageField = document.getElementById("message");
    const counter = document.getElementById("charCounter");

    if (messageField && counter) {
        messageField.addEventListener("input", function () {
            const count = this.value.length;
            counter.textContent = `${count}/1000`;
            counter.style.color = count > 900 ? "red" : "#666";
        });
    }

    // 🔥 STEP 5: Logout
    const logoutBtn = document.querySelector(".logout-button");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function (e) {
            e.preventDefault();
            this.innerHTML = "Logging out...";
            this.style.background = "#ff4d4d";

            setTimeout(() => {
                window.location.href = "/logout";
            }, 800);
        });
    }

    // 🔥 START
    loadUserAndDepartments();
});