<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cineplex.cineplex.model.mo.Film" %>
<%@ include file="/include/header.inc" %>

<%
    Film film = (Film) request.getAttribute("film");
%>

<div class="container mx-auto mt-8">
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="md:flex">
            <div class="md:flex-shrink-0">
                <img class="h-48 w-full object-cover md:w-48" src="<%= film.getPercorsoLocandina() %>" alt="<%= film.getTitolo() %>">
            </div>
            <div class="p-8">
                <div class="uppercase tracking-wide text-sm text-indigo-500 font-semibold"><%= film.getDataPubblicazione().getYear() + 1900 %></div>
                <h1 class="mt-1 text-3xl leading-tight font-bold text-gray-900"><%= film.getTitolo() %></h1>
                <p class="mt-2 text-gray-600"><%= film.getDescrizione() %></p>
                <div class="mt-4">
                    <span class="text-gray-700 font-bold">Director:</span>
                    <span class="text-gray-600"><%= film.getRegista() %></span>
                </div>
                <div class="mt-2">
                    <span class="text-gray-700 font-bold">Duration:</span>
                    <span class="text-gray-600"><%= film.getDurataMinuti() %> minutes</span>
                </div>
                <div class="mt-4">
                    <a href="<%= film.getLinkTrailerYt() %>" target="_blank" class="inline-block bg-indigo-500 text-white px-4 py-2 rounded hover:bg-indigo-600">Watch Trailer</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/include/footer.inc" %>