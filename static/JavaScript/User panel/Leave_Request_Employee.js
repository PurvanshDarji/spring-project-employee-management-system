const API = "http://localhost:8082/leave/api";

let isSubmitting = false; // 🔥 DUPLICATE PREVENTION

document.addEventListener("DOMContentLoaded", function() {
    console.log("🚀 Leave App Loaded");
    
    loadLeaveStatus();
    setupLogout();
    
    // Form submit with duplicate protection
    const form = document.getElementById("leaveForm");
    if (form) {
        form.addEventListener("submit", function(e) {
            e.preventDefault();
            if (!isSubmitting) {
                submitLeave();
            }
        });
    }
});

async function submitLeave() {
    if (isSubmitting) {
        console.log("⏳ Already submitting...");
        return;
    }
    
    isSubmitting = true;
    
    // 🔥 UI LOCK
    const submitBtn = document.querySelector('#leaveForm button[type="submit"]');
    const originalText = submitBtn.textContent;
    submitBtn.disabled = true;
    submitBtn.textContent = "⏳ Submitting...";
    
    const data = {
        leaveType: document.getElementById("leaveType").value,
        fromDate: document.getElementById("fromDate").value,
        toDate: document.getElementById("toDate").value,
        reason: document.getElementById("reason").value
    };

    console.log("📤 SUBMIT DATA:", data);

    if (!data.leaveType || !data.fromDate || !data.toDate || !data.reason) {
        alert("❌ Fill all required fields!");
        resetSubmitBtn();
        return;
    }

    try {
        const response = await fetch(API + "/apply", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        console.log("✅ SAVE RESULT:", result);
        
        if (result.success) {
            alert("✅ Leave Applied Successfully!");
            document.getElementById("leaveForm").reset();
            loadLeaveStatus(); // Immediate reload
        } else {
            alert("❌ " + (result.message || "Failed"));
        }
        
    } catch (error) {
        console.error("❌ ERROR:", error);
        alert("❌ " + error.message);
    } finally {
        resetSubmitBtn();
    }
}

function resetSubmitBtn() {
    isSubmitting = false;
    const submitBtn = document.querySelector('#leaveForm button[type="submit"]');
    submitBtn.disabled = false;
    submitBtn.textContent = "Submit Leave";
}

async function loadLeaveStatus() {
    console.log("🔍 LOADING STATUS...");
    
    const box = document.getElementById("statusBox");
    if (!box) return;

    box.innerHTML = "<h3>Leave Status</h3><p>🔄 Loading...</p>";

    try {
        const response = await fetch(API + "/employee");
        if (!response.ok) throw new Error(await response.text());
        
        const leaves = await response.json();
        console.log("✅ LEAVES:", leaves.length);
        
        box.innerHTML = `<h3>Leave Status (${leaves.length} found)</h3>`;
        
        if (leaves.length === 0) {
            box.innerHTML += "<p>No leave requests</p>";
        } else {
            leaves.forEach((leave, index) => {
                box.innerHTML += `
                    <div class="leave-row">
                        <span>#${index + 1} ${leave.fromDate} → ${leave.toDate}</span>
                        <span class="status ${leave.status?.toLowerCase()}">${leave.status}</span>
                    </div>
                `;
            });
        }
        
    } catch (error) {
        console.error("❌ LOAD ERROR:", error);
        box.innerHTML += `<p class="error">❌ ${error.message}</p>`;
    }
}

function setupLogout() {
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function(e) {
            e.preventDefault();
            logoutBtn.disabled = true;
            logoutBtn.textContent = "Logging out...";
            
            fetch("/logout", { method: "POST" })
            .then(() => window.location.href = "/login")
            .catch(() => window.location.href = "/logout");
        });
    }
}