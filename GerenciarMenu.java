/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Menu;
import model.MenuDAO;

/**
 *
 * @author Pedro
 */
public class GerenciarMenu extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String mensagem = "";

        String acao = request.getParameter("acao");
        String idMenu = request.getParameter("idMenu");

        Menu m = new Menu();

        try {
            MenuDAO mDAO = new MenuDAO();
            if (acao.equals("alterar")) {
                if(GerenciarLogin.verificarPermissao(request, response)){
                m = mDAO.getCarregaPorId(Integer.parseInt(idMenu));
                if (m.getIdMenu() > 0) {
                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_menu.jsp");
                    request.setAttribute("menu", m);
                    disp.forward(request, response);
                } else {
                    mensagem = "ERRO Menu não encontrado";
                }
                }else{
                    mensagem= "Acesso negado";
                }
            }
            if (acao.equals("deletar")) {
                if(GerenciarLogin.verificarPermissao(request, response)){

                m.setIdMenu(Integer.parseInt(idMenu));
                if (mDAO.deletar(m)) {
                    mensagem = "deletado";
                } else {
                    mensagem = "não deletado";
                }
                }else{
                    mensagem = "Acesso negado";
                }
            }
        } catch (Exception e) {
            out.print(e);
            mensagem = "Erro ao executar";
        }
        out.println("<script type='text/javascript'>");
        out.println("alert('" + mensagem + "');");
        out.println("location.href='listar_menu.jsp';");
        out.println("</script>");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String idMenu = request.getParameter("idMenu");
        String nome = request.getParameter("nome");
        String link = request.getParameter("link");
        String icone = request.getParameter("icone");
        String exibir = request.getParameter("exibir");

        String mensagem = "";

        Menu m = new Menu();
        try {
            MenuDAO mDAO = new MenuDAO();
            if (!idMenu.isEmpty()) {
                m.setIdMenu(Integer.parseInt(idMenu));
            }
            if (nome.equals("") || link.equals("") || exibir.equals("")) {
                mensagem = "Campos Obrigátorios deverão ser preenchidos";
            } else {
                m.setNome(nome);
                m.setLink(link);
                m.setIcone(icone);
                m.setExibir(Integer.parseInt(exibir));

                if (mDAO.gravar(m)) {
                    mensagem = "Gravado com sucesso";
                } else {
                    mensagem = "ERRO ao gravar no bd";
                }
            }
        } catch (Exception e) {
            out.print(e);
            mensagem = "Erro ao executar";
        }
        out.println("<script type='text/javascript'>");
        out.println("alert('" + mensagem + "');");
        out.println("location.href='listar_menu.jsp';");
        out.println("</script>");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
