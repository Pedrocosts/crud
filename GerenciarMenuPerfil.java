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
import model.Perfil;
import model.PerfilDAO;

/**
 *
 * @author Pedro
 */
public class GerenciarMenuPerfil extends HttpServlet {


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
        String idPerfil = request.getParameter("idPerfil");

        try {
            Perfil p = new Perfil();
            PerfilDAO pDAO = new PerfilDAO();
            if (acao.equals("gerenciar")) {
                if(GerenciarLogin.verificarPermissao(request, response)){                
                p = pDAO.getCarregaPorId(Integer.parseInt(idPerfil));
                if (p.getIdPerfil() > 0) {
                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_menu_perfil.jsp");
                    request.setAttribute("perfilv", p);
                    disp.forward(request, response);
                } else {
                    mensagem = "ERRO PERFIL não encontrado";
                }
                }else{
                    mensagem = "Acesso negado";
                }
            }
            if (acao.equals("desvincular")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                String idMenu = request.getParameter("idMenu");
                if (idMenu.equals("")){
                    mensagem = "O campo idMenu deve ser selecionado";
                }else{
                    if(pDAO.desvincular(Integer.parseInt(idMenu), Integer.parseInt(idPerfil))){
                        mensagem = "Desvinculado";
                    }else{
                        mensagem = "Erro ao desvincular";
                    }
                }
                }else{
                    mensagem = "Acesso negado"; 
                }
            }
        } catch (Exception e) {
            out.print(e);
            mensagem = "ERRO";
        }
        out.println("<script type='text/javascript'>");
        out.println("alert('"+mensagem+"');");
        out.println("location.href='lista_perfil.jsp';");
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
        String mensagem= ""; 
        String idPerfil = request.getParameter("idPerfil");
        String idMenu = request.getParameter("idMenu");
         Perfil p = new Perfil();
        try {
           
           if(idPerfil.equals("")||idMenu.isEmpty()){
               mensagem = "Campos Obrigátorios deverão ser preenchidos";
           }else{
               PerfilDAO pDAO = new PerfilDAO();
               if(pDAO.vincular(Integer.parseInt(idMenu),Integer.parseInt(idPerfil))){                   
                   mensagem = "Vinculado com sucesso";
               }else{
                   mensagem = "ERRO ao gravar no bd";
               }
           }
        } catch (Exception e) {
             out.print(e);
             mensagem = "Erro ao executar";
        }
        out.println("<script type='text/javascript'>");
        out.println("alert('"+mensagem+"');");
        out.println("location.href='gerenciar_menu_e_perfil.do?acao=gerenciar&idPerfil="+idPerfil+"';");
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
