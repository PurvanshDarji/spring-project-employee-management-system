document.addEventListener('DOMContentLoaded', function() {
    // FAQ Toggle - 100% Working
    document.querySelectorAll('.faq-item').forEach(function(faqItem) {
        faqItem.addEventListener('click', function() {
            const isActive = this.classList.contains('active');
            
            // Close all FAQs first (Accordion style)
            document.querySelectorAll('.faq-item').forEach(function(item) {
                item.classList.remove('active');
                const icon = item.querySelector('.faq-icon');
                const answer = item.querySelector('.faq-answer');
                icon.textContent = '+';
                answer.style.maxHeight = '0px';
            });
            
            // Toggle current FAQ
            if (!isActive) {
                this.classList.add('active');
                const icon = this.querySelector('.faq-icon');
                const answer = this.querySelector('.faq-answer');
                icon.textContent = '−';
                answer.style.maxHeight = answer.scrollHeight + 'px';
            }
        });
    });
    
    console.log('✅ FAQ JavaScript Loaded Successfully!');
});