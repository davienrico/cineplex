<%@ page import="com.cineplex.cineplex.model.mo.Film" %>
<%@ page import="java.util.List" %>
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


<div class="container mx-auto mt-8 px-4">
    <h2 class="text-2xl font-bold mb-6">Featured Films</h2>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <%
            List<Film> films = (List<Film>) request.getAttribute("films");
            if (films != null && !films.isEmpty()) {
                for (Film film : films) {
        %>
        <a href="<%= request.getContextPath() %>/Dispatcher?controllerAction=FilmManagement.viewFilm&filmId=<%= film.getIdFilm() %>" class="block">
            <div class="bg-white rounded-lg shadow-md overflow-hidden h-80 transition duration-300 ease-in-out transform hover:scale-105">
                <img src="<%= request.getContextPath() %><%= film.getPercorsoLocandina() %>"
                     alt="<%= film.getTitolo() %>"
                     class="w-full h-60 object-cover"
                     onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/images/placeholder.jpg';">
                <div class="p-4">
                    <h3 class="font-bold text-lg mb-1 truncate"><%= film.getTitolo() %></h3>
                    <p class="text-gray-600 text-sm"><%= film.getDataPubblicazione().getYear() + 1900 %></p>
                </div>
            </div>
        </a>
        <%
            }
        } else {
        %>
        <p class="col-span-full text-center text-gray-500">No films available at the moment.</p>
        <%
            }
        %>
    </div>
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