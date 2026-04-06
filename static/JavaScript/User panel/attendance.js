const API = "http://localhost:8082/api/attendance";

window.onload = loadTodayStatus;

// ✅ Today ka real status load karo
function loadTodayStatus() {
    let empId = document.getElementById("empIdField").value;

    fetch(API + "/today/" + empId)
    .then(res => res.json())
    .then(data => {
        document.getElementById("todayStatus").innerText = data.status;

        // Buttons show/hide karo state ke hisaab se
        let punchInBtn  = document.getElementById("punchInBtn");
        let punchOutBtn = document.getElementById("punchOutBtn");
        let breakInBtn  = document.getElementById("breakInBtn");
        let breakOutBtn = document.getElementById("breakOutBtn");

        if (data.punchIn === "") {
            // Kuch nahi kiya
            punchInBtn.disabled  = false;
            punchOutBtn.disabled = true;
            breakInBtn.disabled  = true;
            breakOutBtn.disabled = true;

        } else if (data.punchOut !== "") {
            // Punch out ho gaya — sab band
            punchInBtn.disabled  = true;
            punchOutBtn.disabled = true;
            breakInBtn.disabled  = true;
            breakOutBtn.disabled = true;
            document.getElementById("todayStatus").innerText =
                data.status + " (" + data.totalHours + " hrs)";

        } else if (data.onBreak === "true") {
            // Break pe hai
            punchInBtn.disabled  = true;
            punchOutBtn.disabled = true;
            breakInBtn.disabled  = true;
            breakOutBtn.disabled = false;

        } else if (data.breakTaken === "true") {
            // Break le chuka hai — sirf punch out
            punchInBtn.disabled  = true;
            punchOutBtn.disabled = false;
            breakInBtn.disabled  = true;  // ek baar hi break
            breakOutBtn.disabled = true;

        } else {
            // Punched in, break nahi li
            punchInBtn.disabled  = true;
            punchOutBtn.disabled = false;
            breakInBtn.disabled  = false;
            breakOutBtn.disabled = true;
        }

        loadAttendance();
    });
}

function punchIn() {
    let empId = document.getElementById("empIdField").value;
    fetch(API + "/in/" + empId, { method: "POST" })
    .then(res => res.text())
    .then(msg => {
        if (msg === "ALREADY_MARKED") {
            alert("Aaj ki attendance already mark ho chuki hai!");
        } else {
            alert("Punch In successful!");
            loadTodayStatus();
        }
    });
}

function breakStart() {
    let empId = document.getElementById("empIdField").value;
    fetch(API + "/break/start/" + empId, { method: "POST" })
    .then(res => res.text())
    .then(msg => {
        if (msg === "BREAK_ALREADY_TAKEN") {
            alert("Aap ek baar break le chuke ho!");
        } else if (msg === "BREAK_STARTED") {
            alert("Break shuru! 30 min mein wapas aana 😊");
            loadTodayStatus();
        }
    });
}

function breakEnd() {
    let empId = document.getElementById("empIdField").value;
    fetch(API + "/break/end/" + empId, { method: "POST" })
    .then(res => res.text())
    .then(msg => {
        if (msg === "BREAK_ENDED") {
            alert("Welcome back! Break khatam ✅");
            loadTodayStatus();
        }
    });
}

function punchOut() {
    let empId = document.getElementById("empIdField").value;
    fetch(API + "/out/" + empId, { method: "POST" })
    .then(res => res.text())
    .then(msg => {
        if (msg === "NOT_PUNCHED_IN") {
            alert("Pehle Punch In karo!");
        } else if (msg === "ALREADY_PUNCHED_OUT") {
            alert("Aap pehle hi Punch Out kar chuke ho!");
        } else if (msg === "ON_BREAK") {
            alert("Pehle Break End karo, phir Punch Out karo!");
        } else if (msg.startsWith("PUNCHED_OUT:")) {
            let hours = msg.split(":")[1];
            alert("Punch Out successful!\nTotal kaam: " + hours + " ghante");
            loadTodayStatus();
        }
    });
}

function loadAttendance() {
    let empId = document.getElementById("empIdField").value;
    fetch(API + "/employee/" + empId)
    .then(res => res.json())
    .then(data => {
        let p=0, a=0, l=0;
        let html = `<tr><th>Date</th><th>Day</th><th>Punch In</th><th>Break</th><th>Punch Out</th><th>Hours</th><th>Status</th></tr>`;

        data.forEach(x => {
            let d = new Date(x.date);
            let day = d.toLocaleDateString("en-IN", {weekday: 'long'});
            let date = d.toLocaleDateString("en-IN", {day: '2-digit', month: 'short', year: 'numeric'});

            let cls = "";
            if (x.status === "Present")  { cls = "present"; p++; }
            if (x.status === "Absent")   { cls = "absent";  a++; }
            if (x.status === "Half Day") { cls = "leave";   }
            if (x.status === "Leave")    { cls = "leave";   l++; }

            html += `<tr>
                <td>${date}</td>
                <td>${day}</td>
                <td>${x.punchIn  || "-"}</td>
                <td>${x.breakStart ? "30 min" : "No Break"}</td>
                <td>${x.punchOut || "-"}</td>
                <td>${x.totalHours || "-"} hrs</td>
                <td class="${cls}">${x.status}</td>
            </tr>`;
        });

        let total = p + a + l;
        let percent = total ? ((p / total) * 100).toFixed(0) : 0;

        document.getElementById("p").innerText = p;
        document.getElementById("a").innerText = a;
        document.getElementById("l").innerText = l;
        document.getElementById("per").innerText = percent + "%";
        document.getElementById("table").innerHTML = html;
    });
}

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