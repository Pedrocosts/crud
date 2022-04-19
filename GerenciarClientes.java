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
import model.Clientes;
import model.ClientesDAO;

/**
 *
 * @author Pedro
 */
public class GerenciarClientes extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String mensagem = "";

        String acao = request.getParameter("acao");
        String idClientes = request.getParameter("idClientes");

        Clientes m = new Clientes();

        try {
            ClientesDAO mDAO = new ClientesDAO();
            if (acao.equals("alterar")) {
                if (GerenciarLogin.verificarPermissao(request, response)) {
                    m = mDAO.getCarregaPorId(Integer.parseInt(idClientes));
                    if (m.getIdClientes()> 0) {
                        RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_clientes.jsp");
                        request.setAttribute("clientes", m);
                        disp.forward(request, response);
                    } else {
                        mensagem = "ERRO Menu não encontrado";
                    }
                } else {
                    mensagem = "Acesso negado";
                }
            }
            if (acao.equals("deletar")) {
                if (GerenciarLogin.verificarPermissao(request, response)) {

                    m.setIdClientes(Integer.parseInt(idClientes));
                    if (mDAO.deletar(m)) {
                        mensagem = "deletado";
                    } else {
                        mensagem = "não deletado";
                    }
                } else {
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
        String idClientes = request.getParameter("idClientes");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String fone = request.getParameter("fone");

        String mensagem = "";

        Clientes c = new Clientes();
        try {
            ClientesDAO cDAO = new ClientesDAO();
            if (!idClientes.isEmpty()) {
                c.setIdClientes(Integer.parseInt(idClientes));
            }
            if (nome.equals("")) {
                mensagem = "Campos Obrigátorios deverão ser preenchidos";
            } else {
                
                c.setNome(nome);
                c.setEmail(email);
                c.setFone(fone);

                if (cDAO.gravar(c)) {
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
        out.println("location.href='listar_clientes.jsp';");
        out.println("</script>");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
