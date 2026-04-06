// ============================================
// ABOUT PAGE - COMPLETE JAVASCRIPT
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 About Page JavaScript Loaded Successfully!');
    
    // 1. SCROLL REVEAL ANIMATION
    const reveals = document.querySelectorAll(".reveal");
    
    function handleScrollReveal() {
        reveals.forEach(function(el) {
            const windowHeight = window.innerHeight;
            const elementTop = el.getBoundingClientRect().top;
            const elementVisible = 150;
            
            if (elementTop < windowHeight - elementVisible) {
                el.classList.add("active");
            }
        });
    }
    
    // Initial check + scroll listener
    handleScrollReveal();
    window.addEventListener('scroll', handleScrollReveal);
    
    // 2. POPUP FUNCTIONS
    const popupOverlay = document.getElementById('popup');
    
    // Show popup
    window.showWarning = function() {
        if (popupOverlay) {
            popupOverlay.style.display = 'flex';
        }
    };
    
    // Close popup
    window.closePopup = function() {
        if (popupOverlay) {
            popupOverlay.style.display = 'none';
        }
    };
    
    // Close on overlay click
    if (popupOverlay) {
        popupOverlay.addEventListener('click', function(e) {
            if (e.target === this) {
                closePopup();
            }
        });
    }
    
    // Close popup button
    const popupBtn = document.querySelector('.popup-box .btn-popup');
    if (popupBtn) {
        popupBtn.addEventListener('click', function(e) {
            e.preventDefault();
            closePopup();
        });
    }
    
    // 3. KEYBOARD SUPPORT
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && popupOverlay && popupOverlay.style.display === 'flex') {
            closePopup();
        }
    });
    
    // 4. SMOOTH SCROLL FOR ANCHOR LINKS
    document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // 5. CARD HOVER ENHANCEMENT
    document.querySelectorAll('.card').forEach(function(card) {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-15px) scale(1.02)';
        });
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });
    
    // 6. INITIAL PAGE LOAD ANIMATION
    document.body.style.opacity = '0';
    document.body.style.transition = 'opacity 0.5s ease';
    setTimeout(function() {
        document.body.style.opacity = '1';
    }, 100);
});

// ============================================
// GLOBAL FUNCTIONS (Backward Compatibility)
// ============================================
function showWarning() {
    if (typeof window.showWarning === 'function') {
        window.showWarning();
    }
}

function closePopup() {
    if (typeof window.closePopup === 'function') {
        window.closePopup();
    }
}