<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/include/header.inc" %>

<div id="successPopup" class="fixed top-0 left-0 right-0 z-50 hidden">
    <div class="bg-green-500 text-white px-6 py-4 mx-auto mt-4 rounded-lg shadow-lg max-w-md text-center">
        <p>Login successful!</p>
    </div>
</div>

<div class="container mx-auto mt-8">
    <h1 class="text-3xl font-bold mb-4">Welcome to Cineplex</h1>
    <p class="text-lg">Enjoy the latest movies and exclusive content.</p>
</div>

<script>
    window.onload = function() {
        const loginSuccess = <%= request.getAttribute("loginSuccess") != null && (Boolean)request.getAttribute("loginSuccess") %>;
        console.log("Login success:", loginSuccess); // For debugging
        if (loginSuccess) {
            const successPopup = document.getElementById('successPopup');
            successPopup.classList.remove('hidden');
            setTimeout(() => {
                successPopup.classList.add('hidden');
            }, 3000);
        }
    }
</script>

<%@ include file="/include/footer.inc" %>