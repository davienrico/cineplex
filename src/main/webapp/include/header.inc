<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cineplex.cineplex.model.mo.Utente" %>
<%@ page import="com.cineplex.cineplex.model.dao.DAOFactory" %>
<%@ page import="com.cineplex.cineplex.model.dao.UtenteDAO" %>
<%@ page import="com.cineplex.cineplex.services.config.Configuration" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cineplex</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Montserrat', sans-serif;
        }
        .logo {
            color: #4a4a4a;
            font-size: 2rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }
    </style>
</head>
<body class="bg-gray-100">
    <%
        Utente loggedUser = null;
        Cookie[] cookies = request.getCookies();
        String loggedUserCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("LoggedUser")) {
                    loggedUserCookie = cookie.getValue();
                    break;
                }
            }
        }

        if (loggedUserCookie != null && !loggedUserCookie.isEmpty()) {
            DAOFactory daoFactory = null;
            try {
                Map sessionFactoryParameters = new HashMap<String, Object>();
                sessionFactoryParameters.put("request", request);
                sessionFactoryParameters.put("response", response);
                daoFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
                daoFactory.beginTransaction();
                UtenteDAO sessionUserDAO = daoFactory.getUtenteDAO();
                loggedUser = sessionUserDAO.findLoggedUser();
                daoFactory.commitTransaction();
            } catch (Exception e) {
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } finally {
                if (daoFactory != null) daoFactory.closeTransaction();
            }
        }

        if (loggedUser == null) {
            loggedUser = (Utente) session.getAttribute("loggedUser");
        }

        boolean loggedOn = (loggedUser != null);
    %>
<header class="bg-white shadow">
    <div class="container mx-auto px-4 py-4">
        <div class="flex justify-between items-center">
            <a href="${pageContext.request.contextPath}/Dispatcher" class="logo">Cineplex</a>
            <div class="flex items-center space-x-4">
                <% if (!loggedOn) { %>
                    <a href="${pageContext.request.contextPath}/Dispatcher?controllerAction=HomeManagement.loginView"
                       class="login-signup-btn">
                        Login/SignUp
                    </a>
                <% } else { %>
                    <div id="userMenuContainer" class="relative">
                        <button onclick="toggleUserMenu()" class="text-gray-600 hover:text-gray-900 focus:outline-none">
                            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                            </svg>
                        </button>
                        <div id="userDropdown" class="hidden absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10">
                                <a href="${pageContext.request.contextPath}/Dispatcher?controllerAction=UtenteManagement.profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Profilo</a>
                                <a href="${pageContext.request.contextPath}/Dispatcher?controllerAction=BigliettoManagement.viewMyTickets" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">I miei biglietti</a>
                                <a href="${pageContext.request.contextPath}/Dispatcher?controllerAction=RecensioneManagement.insertReview" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Inserisci recensione</a>
                                <a href="${pageContext.request.contextPath}/Dispatcher?controllerAction=AbbonamentoManagement.viewSubscription" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Abbonamento</a>
                                <% if (loggedUser != null && "admin".equalsIgnoreCase(loggedUser.getTipo())) { %>
                                    <a href="${pageContext.request.contextPath}/Dispatcher?controllerAction=AdminManagement.console" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Console admin</a>
                                <% } %>
                                <a href="${pageContext.request.contextPath}/Dispatcher?controllerAction=HomeManagement.logout" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Logout</a>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <style>
        .login-signup-btn {
            background-color: #3490dc;
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 0.25rem;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .login-signup-btn:hover {
            background-color: #2779bd;
        }
    </style>

    <script>
        function toggleUserMenu() {
            const dropdown = document.getElementById('userDropdown');
            dropdown.classList.toggle('hidden');
        }
    </script>
</body>
</html>