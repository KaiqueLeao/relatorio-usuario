package br.com.dasa.relatoriousuario.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.dasa.relatoriousuario.model.RelatorioForm;
import br.com.dasa.relatoriousuario.model.RelatorioPositivacao;
import br.com.dasa.relatoriousuario.service.LaboratorioService;
import br.com.dasa.relatoriousuario.service.PositivacaoNegociacaoService;
import br.com.dasa.relatoriousuario.service.RelatorioNegociacaoService;
import br.com.dasa.relatoriousuario.utils.Util;

@Controller
public class RelatorioController {
	
	static Logger logger = Logger.getLogger(RelatorioController.class);
	
	@Autowired
	private LaboratorioService serviceLaboratorio;
	
	@Autowired
	private PositivacaoNegociacaoService servicePositivacaoNegociacao;
	
	@Autowired
	private RelatorioNegociacaoService serviceRelatorioNegociacao;
	
	@RequestMapping("/")
	public ModelAndView init(){
		ModelAndView mv = new ModelAndView("home");
		mv.addObject("laboratorioList", serviceLaboratorio.todosLaboratorios());
		return mv;
	}
	
	@RequestMapping(value = "/buscar", method = RequestMethod.POST)
	public ResponseEntity<?> gerarRelatorio(@RequestBody RelatorioForm relatorioForm,HttpServletRequest request){
		RelatorioPositivacao relatorioPositivacao = null;
			try {
				relatorioPositivacao = servicePositivacaoNegociacao.relatorioPositivacao(relatorioForm.getDataInicio(), relatorioForm.getDataFinal(), relatorioForm.getMarca(), request);
				serviceRelatorioNegociacao.gerarRelatorioPositivacao(relatorioPositivacao, request);
				return new ResponseEntity<>("Arquivo gerado com sucesso.",HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Erro gerarRelatorio: " + e.getMessage());
				e.printStackTrace();
				return new ResponseEntity<>("Erro ao gerar relatório.",HttpStatus.BAD_REQUEST);
			}
	}
	
	@RequestMapping("/downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {             
		File fileToDownload = null;
		String fileName;
		try {
		/**
		 * Retorna caminho para pasta temporária do SO onde foi salvo o
		 * arquivo .xls
		 */
		HttpSession session = request.getSession(true);
		String caminhoArquivo = (String) session.getAttribute("caminhoArquivo");
			
    	 fileName = caminhoArquivo;

         //Code to download
         fileToDownload = new File(fileName);
         InputStream in = new FileInputStream(fileToDownload);

         // Gets MIME type of the file
         String mimeType = new MimetypesFileTypeMap().getContentType(fileName);

         if (mimeType == null) {
             // Set to binary type if MIME mapping not found
             mimeType = "application/octet-stream";
         }
         logger.info("MIME type: " + mimeType);

         // Modifies response
         response.setContentType(mimeType);
         response.setContentLength((int) fileToDownload.length());

         // Forces download
         String headerKey = "Content-Disposition";
         String headerValue = String.format("attachment; filename=\"%s\"", fileToDownload.getName());
         response.setHeader(headerKey, headerValue);

         // obtains response's output stream
         OutputStream outStream = response.getOutputStream();

         byte[] buffer = new byte[4096];
         int bytesRead = -1;

         while ((bytesRead = in.read(buffer)) != -1) {
             outStream.write(buffer, 0, bytesRead);
         }

         in.close();
         outStream.close();

         /**
          * Exclui arquivo após download
          */
         Util.excluirArquivo(fileToDownload);

     } catch (Exception ex) {
         logger.error("Erro downloadFile: " + ex.getMessage());
     }
 }
	
}
