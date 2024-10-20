<%@ page import="com.cineplex.cineplex.model.mo.Film" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/include/header.inc" %>

<div id="successPopup" class="fixed top-0 left-0 right-0 z-50 hidden">
    <div class="bg-green-500 text-white px-6 py-4 mx-auto mt-4 rounded-lg shadow-lg max-w-md text-center">
        <p>Login successful!</p>
    </div>
</div>


<div class="container mx-auto mt-8 px-4">
    <h2 class="text-3xl font-bold mb-6 text-center">Ricerca Film</h2>
    <form action="${pageContext.request.contextPath}/Dispatcher" method="get" class="mb-8">
        <input type="hidden" name="controllerAction" value="HomeManagement.searchFilms">
        <div class="flex flex-col md:flex-row gap-4">
            <div class="flex-grow">
                <input type="text" name="title" placeholder="Ricerca titolo" class="w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>
            <div>
                <select name="date" class="w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="">Seleziona data</option>
                    <!-- We'll populate this with JavaScript -->
                </select>
            </div>
            <button type="submit" class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition duration-300">Search</button>
        </div>
    </form>

    <!-- Search Results Section -->
    <% if (request.getAttribute("searchPerformed") != null && (Boolean)request.getAttribute("searchPerformed")) { %>
    <h2 class="text-3xl font-bold mb-8 text-center">Risultati Ricerca</h2>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 mb-12">
        <%
            List<Film> searchResults = (List<Film>) request.getAttribute("films");
            if (searchResults != null && !searchResults.isEmpty()) {
                for (Film film : searchResults) {
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
        <p class="col-span-full text-center text-gray-500">Nessun film trovato.</p>
        <%
            }
        %>
    </div>
    <% } %>

    <!-- Featured Films Section -->
    <h2 class="text-3xl font-bold mb-8 text-center">Film in programmazione</h2>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <%
            List<Film> featuredFilms = (List<Film>) request.getAttribute("featuredFilms");
            if (featuredFilms != null && !featuredFilms.isEmpty()) {
                for (Film film : featuredFilms) {
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
        <p class="col-span-full text-center text-gray-500">Nessun film in programmazione.</p>
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

        // Populate the date dropdown
        const dateSelect = document.querySelector('select[name="date"]');
        const today = new Date();
        for (let i = 0; i < 7; i++) {
            const date = new Date(today);
            date.setDate(today.getDate() + i);
            const option = document.createElement('option');
            option.value = date.toISOString().split('T')[0]; // YYYY-MM-DD format
            option.textContent = date.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' });
            dateSelect.appendChild(option);
        }
    }
</script>

<%@ include file="/include/footer.inc" %>