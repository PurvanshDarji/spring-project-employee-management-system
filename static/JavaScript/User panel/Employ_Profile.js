document.addEventListener("DOMContentLoaded", function () {

const editBtn = document.getElementById("editBtn");
const saveBtn = document.getElementById("saveBtn");
const cancelBtn = document.getElementById("cancelBtn");

const name = document.getElementById("name");
const phone = document.getElementById("phone");
const password = document.getElementById("password");

// ✅ EDIT BUTTON
editBtn.addEventListener("click", function () {

name.disabled = false;
phone.disabled = false;
password.disabled = false;

editBtn.style.display = "none";
saveBtn.style.display = "inline-block";
cancelBtn.style.display = "inline-block";

});

// ✅ SAVE BUTTON
saveBtn.addEventListener("click", function () {

fetch("/employee/update-profile", {

method: "POST",

headers: {
"Content-Type":
"application/x-www-form-urlencoded"
},

body:
"name=" + encodeURIComponent(name.value)
+ "&phone=" + encodeURIComponent(phone.value)
+ "&password=" + encodeURIComponent(password.value)

})
.then(response => response.text())
.then(data => {

if (data === "success") {

alert("Profile Updated ✅");
location.reload();

} else {

alert("Update Failed");

}

})
.catch(error => console.log(error));

});

// ✅ CANCEL
cancelBtn.addEventListener("click", function () {

location.reload();

});

});

document.addEventListener("DOMContentLoaded", function() {

    const btn = document.getElementById("logoutBtn");

    if(btn){
        btn.addEventListener("click", function(){

            btn.style.transform = "scale(0.9)";
            btn.style.background = "#ff4d4d";

            setTimeout(()=>{
                btn.style.transform = "scale(1)";
            },200);

            setTimeout(()=>{
                window.location.href="/logout";
            },500);

        });
    }

});