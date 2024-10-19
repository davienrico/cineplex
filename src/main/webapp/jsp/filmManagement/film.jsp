<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cineplex.cineplex.model.mo.Film" %>
<%@ include file="/include/header.inc" %>

<%
    Film film = (Film) request.getAttribute("film");
%>

<div class="container mx-auto mt-8 px-4">
    <div class="flex flex-col md:flex-row md:space-x-8">
        <!-- Left column: Poster and Title -->
        <div class="md:w-1/3 mb-6 md:mb-0 flex flex-col">
            <div class="relative rounded-lg overflow-hidden flex-grow">
                <img src="<%= request.getContextPath() %><%= film.getPercorsoLocandina() %>" alt="<%= film.getTitolo() %>" class="w-full h-full object-cover">
                <div class="absolute inset-0 flex items-end justify-center bg-gradient-to-t from-black to-transparent">
                    <h1 class="text-3xl font-bold text-white text-center px-4 pb-4" style="text-shadow: 2px 2px 4px rgba(0,0,0,0.8);">
                        <%= film.getTitolo() %>
                    </h1>
                </div>
            </div>
        </div>

        <!-- Right column: Info and Trailer -->
        <div class="md:w-2/3 flex flex-col">
            <div class="bg-white rounded-lg shadow-md p-6 flex-grow flex flex-col">
                <h2 class="text-2xl font-semibold mb-4">Film Details</h2>
                <p class="mb-2"><strong>Duration:</strong> <%= film.getDurataMinuti() %> minutes</p>
                <p class="mb-4"><strong>Director:</strong> <%= film.getRegista() %></p>
                <div class="mb-6">
                    <h3 class="font-semibold mb-2">Description</h3>
                    <p><%= film.getDescrizione() %></p>
                </div>

                <h3 class="text-xl font-semibold mb-4">Trailer</h3>
                <div class="relative flex-grow" style="padding-top: 56.25%;">
                    <iframe
                            src="<%= film.getLinkTrailerYt() %>"
                            frameborder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                            allowfullscreen
                            class="absolute top-0 left-0 w-full h-full rounded-lg"
                    ></iframe>
                </div>

                <!-- Buy Tickets Button -->
                <a href="#" class="block w-full bg-blue-500 text-white text-center font-bold py-3 px-4 rounded-lg hover:bg-blue-600 transition duration-300 mt-6">
                    Buy Tickets
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/include/footer.inc" %>