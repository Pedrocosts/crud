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
import model.Os;
import model.OsDAO;

/**
 *
 * @author Pedro
 */
public class GerenciarOs extends HttpServlet {


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
        String idOs = request.getParameter("idOs");
        
        Os o = new Os();
        
        try {
             OsDAO oDAO = new OsDAO();
            if (acao.equals("alterar")) {
                if(GerenciarLogin.verificarPermissao(request, response)){
                o = oDAO.getCarregaPorId(Integer.parseInt(idOs));
                if (o.getIdOs()> 0) {
                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_os.jsp");
                    request.setAttribute("os", o);
                    disp.forward(request, response);
                } else {
                    mensagem = "ERRO, não encontrado";
                }
                }else{
                    mensagem= "Acesso negado";
                }
            }
            if (acao.equals("deletar")) {
                if(GerenciarLogin.verificarPermissao(request, response)){

                o.setIdOs(Integer.parseInt(idOs));
                if (oDAO.deletar(o)) {
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
        out.println("location.href='listar_os.jsp';");
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
        String idOs = request.getParameter("idOs");
        String data_os = request.getParameter("data_os");
        String equipamento = request.getParameter("equipamento");
        String servico = request.getParameter("servico");
        String valor = request.getParameter("valor");
        String tecnico = request.getParameter("tecnico");
        String tipo = request.getParameter("tipo");
        String situacao = request.getParameter("situacao");
        String idClientes = request.getParameter("idClientes");
        String mensagem ="";
        
        Os o = new Os();
        try {
           OsDAO oDAO = new OsDAO();
           if(!idOs.isEmpty()){
               o.setIdOs(Integer.parseInt(idOs));
           }
           if(data_os.equals("")||equipamento.equals("")||servico.equals("")||valor.equals("")||tecnico.equals("")
                   ||tipo.equals("")||situacao.equals("")){
               mensagem = "Campos Obrigátorios deverão ser preenchidos";
           }else{
               o.setData_os(data_os);
               o.setEquipamento(equipamento);
               o.setServico(servico);
               o.setValor(valor);
               o.setTecnico(tecnico);
               o.setTipo(tipo);
               o.setSituacao(situacao);
               Clientes c = new Clientes();
               c.setIdClientes(Integer.parseInt(idClientes));
               o.setClientes(c);
               if(oDAO.gravar(o)){                   
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
        out.println("location.href='listar_os.jsp';");
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
