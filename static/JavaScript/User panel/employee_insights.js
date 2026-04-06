// ✅ Database se aaye values HTML se lo
const attVal    = parseInt(document.getElementById("att").innerText)    || 0;
const leaveVal  = parseInt(document.getElementById("leave").innerText)  || 0;
const perfVal   = parseInt(document.getElementById("perf").innerText)   || 0;
const salaryVal = parseInt(document.getElementById("salary").innerText) || 0;

// ✅ Cards animate karo
function animate(id, start, end, speed, suffix="") {
    let val = start;
    const el = document.getElementById(id);
    if (!el) return;

    // Reset first
    el.innerText = start + suffix;

    const timer = setInterval(() => {
        val++;
        el.innerText = val + suffix;
        if (val >= end) clearInterval(timer);
    }, speed);
}

animate("att",    0, attVal,    20,  "%");
animate("leave",  0, leaveVal,  150, "");
animate("perf",   0, perfVal,   25,  "%");
animate("salary", 0, salaryVal, 40,  "%");

// ✅ Progress bars animate karo
setTimeout(() => {
    document.querySelectorAll(".progress-fill").forEach(bar => {
        let target = parseInt(bar.getAttribute("data-value")) || 0;
        bar.style.width = "0%";
        bar.innerText = "0%";

        let current = 0;
        let interval = setInterval(() => {
            if (current >= target) {
                clearInterval(interval);
            } else {
                current++;
                bar.style.width = current + "%";
                bar.innerText = current + "%";
            }
        }, 15);
    });
}, 600);

// ✅ Logout button
document.addEventListener("DOMContentLoaded", function () {
    const btn = document.getElementById("logoutBtn");
    if (btn) {
        btn.addEventListener("click", function () {
            btn.style.transform = "scale(0.9)";
            btn.style.background = "#ff4d4d";
            setTimeout(() => { btn.style.transform = "scale(1)"; }, 200);
            setTimeout(() => { window.location.href = "/logout"; }, 500);
        });
    }
});