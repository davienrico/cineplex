<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/include/header.inc" %>

<div class="container mx-auto mt-8">
    <h1 class="text-3xl font-bold mb-4">Welcome to Cineplex</h1>
    <p class="text-lg">Enjoy the latest movies and exclusive content.</p>


</div>

<%
    Boolean logoutSuccess = (Boolean) request.getAttribute("logoutSuccess");
    if (logoutSuccess != null && logoutSuccess) {
%>
<div id="logoutPopup" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3 text-center">
            <h3 class="text-lg leading-6 font-medium text-gray-900">Logout successful</h3>
            <div class="mt-2 px-7 py-3">
                <button id="closeLogoutPopup" class="px-4 py-2 bg-blue-500 text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-300">
                    OK
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('closeLogoutPopup').addEventListener('click', function() {
        document.getElementById('logoutPopup').style.display = 'none';
    });
</script>
<%
    }
%>

<%@ include file="/include/footer.inc" %>