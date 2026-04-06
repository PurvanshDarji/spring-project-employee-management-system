/* ========================================
   COMPLETE LOGIN.JS - 100% WORKING VERSION
=========================================== */

// Toggle Password
function togglePass(){
    let pass = document.getElementById("password");
    let toggle = document.querySelector(".toggle");
    
    if(pass.type === "password"){
        pass.type = "text";
        toggle.textContent = "🙈";
    } else {
        pass.type = "password";
        toggle.textContent = "👁";
    }
}

// 🔥 MAIN LOGIN FUNCTION - FIXED
function login() {
    let username = document.getElementById("username").value.trim();
    let password = document.getElementById("password").value.trim();
    let error = document.getElementById("error");
    let box = document.getElementById("loginBox");
    let loginBtn = document.getElementById("loginBtn");

    // Reset
    error.style.display = "none";
    box.classList.remove("shake");

    // Validation
    if(username === "" || password === "") {
        showError("Please enter phone number and password!", box);
        return;
    }

    // Loading
    loginBtn.innerHTML = "Logging in...";
    loginBtn.disabled = true;

    // 🔥 AJAX LOGIN
    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "username=" + encodeURIComponent(username) + 
              "&password=" + encodeURIComponent(password)
    })
    .then(response => {
        if (response.redirected) {
            // SUCCESS - Follow redirect
            window.location.href = response.url;
        } else {
            // FAILURE
            loginBtn.innerHTML = "Log In";
            loginBtn.disabled = false;
            throw new Error("Invalid credentials");
        }
    })
    .catch(error => {
        console.error("Login Error:", error);
        showError("Invalid phone number or password!", box);
        loginBtn.innerHTML = "Log In";
        loginBtn.disabled = false;
    });
}

function showError(message, box) {
    let error = document.getElementById("error");
    error.innerText = message;
    error.style.display = "block";
    
    box.classList.add("shake");
    setTimeout(() => box.classList.remove("shake"), 400);
}

// Enter Key
document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        login();
    }
});

/* ============================================
   ENHANCED EYES ANIMATION (Your Original)
=========================================== */
(function() {
    'use strict';
    
    const EYES_CONFIG = {
        MAX_MOVE: 9,
        BLINK_INTERVAL: 2500,
        BLINK_DURATION: 0.12,
        FOLLOW_SMOOTHNESS: 0.15,
        HEAD_TILT_MAX: 5
    };
    
    let eyesLocked = false;
    let mouseX = 0, mouseY = 0;
    let currentEyeX = 0, currentEyeY = 0;
    
    const allEyes = document.querySelectorAll(".eyes span");
    const allHeads = document.querySelectorAll(".shape");
    const mouths = document.querySelectorAll(".mouth");

    document.addEventListener("mousemove", function(e) {
        if (eyesLocked) return;
        mouseX = e.clientX;
        mouseY = e.clientY;
        
        allEyes.forEach(function(eye, index) {
            const rect = eye.getBoundingClientRect();
            const cx = rect.left + rect.width / 2;
            const cy = rect.top + rect.height / 2;
            const dx = mouseX - cx;
            const dy = mouseY - cy;
            const angle = Math.atan2(dy, dx);
            
            currentEyeX += (Math.cos(angle) * EYES_CONFIG.MAX_MOVE - currentEyeX) * EYES_CONFIG.FOLLOW_SMOOTHNESS;
            currentEyeY += (Math.sin(angle) * EYES_CONFIG.MAX_MOVE - currentEyeY) * EYES_CONFIG.FOLLOW_SMOOTHNESS;
            
            eye.style.transform = `translate(${currentEyeX}px, ${currentEyeY}px) scale(${1 + (Math.sin(Date.now() * 0.003 + index) * 0.05)})`;
        });
    });
    
    function blinkAll(pattern = 'normal') {
        if (eyesLocked) return;
        const patterns = {
            normal: { scaleY: 0.15, duration: 0.1 },
            sleepy: { scaleY: 0.05, duration: 0.25 },
            surprised: { scaleY: 0.3, duration: 0.08 }
        };
        const config = patterns[pattern] || patterns.normal;
        
        gsap.to(allEyes, {
            scaleY: config.scaleY,
            duration: config.duration,
            yoyo: true,
            repeat: 1,
            ease: "power2.inOut",
            stagger: { amount: 0.1, from: "random" }
        });
    }
    
    const blinkPatterns = ['normal', 'sleepy', 'surprised'];
    setInterval(() => {
        const randomPattern = blinkPatterns[Math.floor(Math.random() * blinkPatterns.length)];
        blinkAll(randomPattern);
    }, EYES_CONFIG.BLINK_INTERVAL);

    // Page Load Animation
    gsap.from(".shape", {
        opacity: 0,
        scale: 0.5,
        y: 100,
        duration: 0.8,
        stagger: 0.15,
        ease: "back.out(1.7)"
    });
    
    gsap.from(".login-box", {
        opacity: 0,
        x: 100,
        duration: 0.8,
        delay: 0.3,
        ease: "back.out(1.7)"
    });
})();