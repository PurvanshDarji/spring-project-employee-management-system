document.addEventListener("DOMContentLoaded", loadSalary);

async function loadSalary() {
    try {
        const response = await fetch("/api/salary/data"); // ✅ FIXED URL

        if (!response.ok) {
            throw new Error("Server error: " + response.status);
        }

        const salary = await response.json();
        console.log("Salary data:", salary); // Debug ke liye

        // ✅ Salary.java ke exact field names use kar rahe hain
        document.getElementById("empId").textContent =
            "EMP" + salary.empId;

        document.getElementById("empName").textContent =
            salary.empName;

        document.getElementById("department").textContent =
            salary.department;

        // Navbar
        const userInfo = document.getElementById("userInfo");
        if (userInfo) {
            userInfo.textContent = salary.empName + " • EMP" + salary.empId;
        }

        // ✅ Exact field names: allowance, deduction (s nahi)
        animate("base", salary.baseSalary);
        animate("allow", salary.allowance);
        animate("deduct", salary.deduction);
        animateNet("net", salary.netSalary);

        // Progress Bar
        setTimeout(() => {
            const bar = document.getElementById("bar");
            if (bar) bar.style.width = "80%";
        }, 300);

    } catch (error) {
        console.error("Salary loading failed:", error);
        fallbackAnimation();
    }
}

function animate(id, target) {
    let el = document.getElementById(id);
    if (!el) return; // element nahi mila toh crash mat karo
    let count = 0;
    let speed = target / 60;
    function run() {
        if (count < target) {
            count += speed;
            el.innerText = Math.floor(count);
            requestAnimationFrame(run);
        } else {
            el.innerText = Math.floor(target);
        }
    }
    run();
}

function animateNet(id, target) {
    let el = document.getElementById(id);
    if (!el) return;
    let count = 0;
    let speed = target / 60;
    function run() {
        if (count < target) {
            count += speed;
            el.innerText = "₹ " + Math.floor(count);
            requestAnimationFrame(run);
        } else {
            el.innerText = "₹ " + Math.floor(target);
        }
    }
    run();
}

function fallbackAnimation() {
    animate("base", 25000);
    animate("allow", 4000);
    animate("deduct", 2000);
    animateNet("net", 27000);
}

// Logout Button
document.addEventListener("DOMContentLoaded", function () {
    const btn = document.getElementById("logoutBtn");
    if (btn) {
        btn.addEventListener("click", function () {
            btn.style.transform = "scale(0.9)";
            setTimeout(() => { btn.style.transform = "scale(1)"; }, 200);
            setTimeout(() => { window.location.href = "/logout"; }, 500);
        });
    }
});