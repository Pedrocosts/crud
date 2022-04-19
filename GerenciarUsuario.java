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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Perfil;
import model.Usuario;
import model.UsuarioDAO;

/**
 *
 * @author Pedro
 */
@WebServlet(name = "GerenciarUsuario", urlPatterns = {"/gerenciar_usuario.do"})
public class GerenciarUsuario extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

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
        String idUsuario = request.getParameter("idUsuario");
        
        Usuario u = new Usuario();
        
        try {
            UsuarioDAO uDAO = new UsuarioDAO();
            if(acao.equals("alterar")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                u = uDAO.getCarregaPorId(Integer.parseInt(idUsuario));
                if(u.getIdUsuario()>0){
                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_usuario.jsp");
                    request.setAttribute("usuario", u);
                    disp.forward(request, response);
                }else{
                    mensagem = "ERRO Usuario não encontrado";
                }
                }else{
                    mensagem = "Acesso negado";
                }
            }
            if(acao.equals("deletar")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                u.setIdUsuario(Integer.parseInt(idUsuario));
                if (uDAO.deletar(u)) {
                    mensagem = "Desativado";       
                }else{
                    mensagem = "Não pode ser desativado";
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
        out.println("alert('"+mensagem+"');");
        out.println("location.href='listar_usuario.jsp';");
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
        String idUsuario = request.getParameter("idUsuario");
        String nome = request.getParameter("nome");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        String status = request.getParameter("status");
        String idPerfil = request.getParameter("perfil_idPerfil");
        String mensagem ="";
        
        Usuario u = new Usuario();
        try {
           UsuarioDAO uDAO = new UsuarioDAO();
           if(!idUsuario.isEmpty()){
               u.setIdUsuario(Integer.parseInt(idUsuario));
           }
           if(nome.equals("")||login.equals("")||status.equals("")||senha.equals("")||idPerfil.equals("")){
               mensagem = "Campos Obrigátorios deverão ser preenchidos";
           }else{
               u.setNome(nome);
               u.setLogin(login);
               u.setSenha(senha);
               u.setStatus(Integer.parseInt(status));
               Perfil p = new Perfil();
               p.setIdPerfil(Integer.parseInt(idPerfil));
               u.setPerfil(p);
               if(uDAO.gravar(u)){                   
                   mensagem = "Gravado com sucesso";
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
        out.println("location.href='listar_usuario.jsp';");
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
