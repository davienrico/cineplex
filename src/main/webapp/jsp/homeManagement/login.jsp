<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/include/header.inc" %>

<div class="container mx-auto mt-8">
    <div class="max-w-md mx-auto bg-white rounded-lg overflow-hidden md:max-w-lg">
        <div class="md:flex">
            <div class="w-full px-6 py-8">
                <div id="loginForm">
                    <h2 class="text-center text-3xl font-bold mb-4">Login</h2>
                    <form action="${pageContext.request.contextPath}/Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="HomeManagement.login">
                        <div class="mb-4">
                            <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Username</label>
                            <input type="text" id="username" name="username" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        </div>
                        <div class="mb-6">
                            <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
                            <input type="password" id="password" name="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline" required>
                        </div>
                        <div class="flex items-center justify-between">
                            <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                                Login
                            </button>
                            <a href="#" onclick="toggleForms()" class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">
                                Sign Up
                            </a>
                        </div>
                    </form>
                </div>

                <div id="signupForm" class="hidden">
                    <h2 class="text-center text-3xl font-bold mb-4">Sign Up</h2>
                    <form action="${pageContext.request.contextPath}/Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="HomeManagement.signup">
                        <div class="mb-4">
                            <label for="nome" class="block text-gray-700 text-sm font-bold mb-2">Nome</label>
                            <input type="text" id="nome" name="nome" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        </div>
                        <div class="mb-4">
                            <label for="cognome" class="block text-gray-700 text-sm font-bold mb-2">Cognome</label>
                            <input type="text" id="cognome" name="cognome" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        </div>
                        <div class="mb-4">
                            <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email</label>
                            <input type="email" id="email" name="email" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        </div>
                        <div class="mb-4">
                            <label for="signupUsername" class="block text-gray-700 text-sm font-bold mb-2">Username</label>
                            <input type="text" id="signupUsername" name="username" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        </div>
                        <div class="mb-4">
                            <label for="signupPassword" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
                            <input type="password" id="signupPassword" name="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        </div>
                        <div class="mb-4">
                            <label class="flex items-center">
                                <input type="checkbox" name="newsletter" class="form-checkbox">
                                <span class="ml-2 text-gray-700 text-sm">Subscribe to newsletter</span>
                            </label>
                        </div>
                        <div class="flex items-center justify-between">
                            <button type="submit" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                                Sign Up
                            </button>
                            <a href="#" onclick="toggleForms()" class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">
                                Back to Login
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function toggleForms() {
        const loginForm = document.getElementById('loginForm');
        const signupForm = document.getElementById('signupForm');

        loginForm.classList.toggle('hidden');
        signupForm.classList.toggle('hidden');
    }
</script>

<%@ include file="/include/footer.inc" %>