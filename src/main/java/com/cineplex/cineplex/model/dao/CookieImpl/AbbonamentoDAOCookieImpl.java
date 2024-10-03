package com.cineplex.cineplex.model.dao.CookieImpl;

import com.cineplex.cineplex.model.dao.AbbonamentoDAO;
import com.cineplex.cineplex.model.mo.Abbonamento;
import com.cineplex.cineplex.model.mo.TipoAbbonamento;
import com.cineplex.cineplex.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbbonamentoDAOCookieImpl implements AbbonamentoDAO {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public AbbonamentoDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Abbonamento create(int idAbbonamento, Date dataSottoscrizione, Utente utenteId, TipoAbbonamento tipoAbbonamentoId) {
        Abbonamento abbonamento = new Abbonamento(idAbbonamento, dataSottoscrizione, utenteId, tipoAbbonamentoId, false);
        String encodedAbbonamento = encode(abbonamento);
        Cookie cookie = new Cookie("abbonamento", encodedAbbonamento);
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(cookie);
        return abbonamento;
    }

    @Override
    public Abbonamento create(Integer idAbbonamento, Date dataSottoscrizione, Utente utenteId, TipoAbbonamento tipoAbbonamentoId) {
        return null;
    }

    @Override
    public void update(Abbonamento abbonamento) {
        String encodedAbbonamento = encode(abbonamento);
        Cookie cookie = new Cookie("abbonamento", encodedAbbonamento);
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(cookie);
    }

    @Override
    public void delete(Abbonamento abbonamento) {
        Cookie cookie = new Cookie("abbonamento", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public Abbonamento FindById(Integer idAbbonamento) {
        return null;
    }

    @Override
    public Abbonamento FindById(int idAbbonamento) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("abbonamento")) {
                    Abbonamento abbonamento = decode(cookie.getValue());
                    if (abbonamento.getIdAbbonamento() == idAbbonamento) {
                        return abbonamento;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Abbonamento FindByUserId(Utente utente) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("abbonamento")) {
                    Abbonamento abbonamento = decode(cookie.getValue());
                    if (abbonamento.getUtenteId().getIdUtente() == utente.getIdUtente()) {
                        return abbonamento;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Abbonamento FindByDate(Date dataSottoscrizione) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("abbonamento")) {
                    Abbonamento abbonamento = decode(cookie.getValue());
                    if (abbonamento.getDataSottoscrizione().equals(dataSottoscrizione)) {
                        return abbonamento;
                    }
                }
            }
        }
        return null;
    }

    private String encode(Abbonamento abbonamento) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return abbonamento.getIdAbbonamento() + "#" +
                sdf.format(abbonamento.getDataSottoscrizione()) + "#" +
                abbonamento.getUtenteId().getIdUtente() + "#" +
                abbonamento.getTipoAbbonamentoId().getIdTipoAbbonamento();
    }

    private Abbonamento decode(String encodedAbbonamento) {
        String[] values = encodedAbbonamento.split("#");
        if (values.length >= 4) {
            try {
                int idAbbonamento = Integer.parseInt(values[0]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dataSottoscrizione = sdf.parse(values[1]);
                Utente utente = new Utente();
                utente.setIdUtente(Integer.parseInt(values[2]));
                TipoAbbonamento tipoAbbonamento = new TipoAbbonamento();
                tipoAbbonamento.setIdTipoAbbonamento(Integer.parseInt(values[3]));
                return new Abbonamento(idAbbonamento, dataSottoscrizione, utente, tipoAbbonamento, false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}