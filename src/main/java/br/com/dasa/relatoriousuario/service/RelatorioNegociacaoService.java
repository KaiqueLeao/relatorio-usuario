package br.com.dasa.relatoriousuario.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import br.com.dasa.relatoriousuario.model.ItemPorEspecialidade;
import br.com.dasa.relatoriousuario.model.ItemPorOperador;
import br.com.dasa.relatoriousuario.model.ItemPorUnidade;
import br.com.dasa.relatoriousuario.model.ItemPorUnidadeItem;
import br.com.dasa.relatoriousuario.model.RelatorioPositivacao;
import br.com.dasa.relatoriousuario.utils.Util;

@Component
public class RelatorioNegociacaoService {
	
	public final Logger logger = Logger.getLogger(RelatorioNegociacaoService.class);

	private static String fileName;
	private static HSSFCellStyle styleTitulo;
	private static HSSFCellStyle styleGrade1;
	private static HSSFCellStyle styleGrade2;
	
	/**
	 * @param relatorioPositivacao
	 */
	public void gerarRelatorioPositivacao(RelatorioPositivacao relatorioPositivacao, HttpServletRequest request) {

		fileName = Util.getFilePath(request);
		logger.info("FileName: " + fileName);
		
		// Create an instance of workbook and sheet
		HSSFWorkbook workbook = new HSSFWorkbook();
		setStyle(workbook);

		gerarPorOperador(relatorioPositivacao, workbook);
		gerarPorUnidade(relatorioPositivacao, workbook);
		gerarPorEspecialidade(relatorioPositivacao, workbook);

		// Finally we write out the workbook into an excel file.
		try (FileOutputStream fos = new FileOutputStream(new File(fileName))) {
			workbook.write(fos);
			logger.info("Arquivo " + fileName + "  criado com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// FORMATAÇÃO:
	private static void setStyle(HSSFWorkbook workbook) {
		HSSFPalette pallete = workbook.getCustomPalette();
		pallete.setColorAtIndex(HSSFColor.GOLD.index, (byte) 68, (byte) 114, (byte) 196);

		styleTitulo = workbook.createCellStyle();
		styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleTitulo.setFillForegroundColor(HSSFColor.GOLD.index);
		styleTitulo.setBorderTop(BorderStyle.THIN);
		styleTitulo.setBorderBottom(BorderStyle.THIN);
		styleTitulo.setBorderLeft(BorderStyle.THIN);
		styleTitulo.setBorderRight(BorderStyle.THIN);

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setColor(IndexedColors.WHITE.index);
		font.setBold(true);
		styleTitulo.setFont(font);

		pallete.setColorAtIndex(HSSFColor.CORNFLOWER_BLUE.index, (byte) 180, (byte) 198, (byte) 231);
		pallete.setColorAtIndex(HSSFColor.DARK_YELLOW.index, (byte) 217, (byte) 225, (byte) 242);

		styleGrade1 = workbook.createCellStyle();
		styleGrade1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleGrade1.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
		styleGrade1.setBorderTop(BorderStyle.THIN);
		styleGrade1.setBorderBottom(BorderStyle.THIN);
		styleGrade1.setBorderLeft(BorderStyle.THIN);
		styleGrade1.setBorderRight(BorderStyle.THIN);

		font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		styleGrade1.setFont(font);

		styleGrade2 = workbook.createCellStyle();
		styleGrade2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleGrade2.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
		styleGrade2.setBorderTop(BorderStyle.THIN);
		styleGrade2.setBorderBottom(BorderStyle.THIN);
		styleGrade2.setBorderLeft(BorderStyle.THIN);
		styleGrade2.setBorderRight(BorderStyle.THIN);

		font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		styleGrade2.setFont(font);
	}

	private static void gerarPorUnidade(RelatorioPositivacao porOperador, HSSFWorkbook workbook) {
		HSSFSheet sheet = workbook.createSheet("Por Unidade");

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setColor(IndexedColors.WHITE.index);
		font.setBold(true);
		styleTitulo.setFont(font);

		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBold(false);
		style.setFont(font);

		int rownum = 0;

		HSSFRow row;
		HSSFCell cell;
		for (ItemPorUnidade unidade : porOperador.getItensPorUnidade()) {
			rownum = itemPorUnidade(sheet, styleTitulo, style, rownum, unidade);
		}

		rownum = rownum + 3;
		gerarLegenda(porOperador, workbook, sheet, styleTitulo, style, rownum, 2);

		for (int i = 0; i < 10; i++) {
			sheet.autoSizeColumn((short) i);
		}
	}

	private static void gerarPorOperador(RelatorioPositivacao porOperador, HSSFWorkbook workbook) {
		HSSFSheet sheet = workbook.createSheet("Por Operador");

		int rownum = 0;
		HSSFRow row = sheet.createRow(rownum++);
		tituloPorOperador(styleTitulo, row);

		int position = 0;
		for (ItemPorOperador obj : porOperador.getItensPorOperador()) {
			row = sheet.createRow(rownum++);
			if (++position % 2 == 0) {
				itemPorOperador(styleGrade1, row, obj);
			} else {
				itemPorOperador(styleGrade2, row, obj);
			}

		}

		rownum = rownum + 3;
		gerarLegenda(porOperador, workbook, sheet, styleTitulo, styleGrade1, rownum, 4);

		for (int i = 0; i < 10; i++) {
			sheet.autoSizeColumn((short) i);
		}
	}

	private static void gerarPorEspecialidade(RelatorioPositivacao porOperador, HSSFWorkbook workbook) {
		HSSFSheet sheet = workbook.createSheet("Por Especialidade");
		
		int rownum = 0;
		HSSFRow row = sheet.createRow(rownum++);
		tituloPorEspecialidade(styleTitulo, row);
		
		/**
		 * Aplica estilos (formato, borda, cor) na geração do excel
		 */
		HSSFCellStyle style = formatacaoExcel(workbook);

		int position = 0;
		for (ItemPorEspecialidade obj : porOperador.getItemPorEspecialidade()) {
			row = sheet.createRow(rownum++);
			if (++position % 2 == 0) {
				itemPorEspecialidade(styleGrade1, row, obj);
			} else {
				itemPorEspecialidade(styleGrade2, row, obj);
			}
		}

		rownum = rownum + 3;
		gerarLegenda(porOperador, workbook, sheet, styleTitulo, style, rownum, 2);

		for (int i = 0; i < 10; i++) {
			sheet.autoSizeColumn((short) i);
		}
	}

	private static HSSFCellStyle formatacaoExcel(HSSFWorkbook workbook) {
		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.index);
		styleTitulo.setFont(font);

		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBold(false);
		style.setFont(font);
		return style;
	}

	private static void gerarLegenda(RelatorioPositivacao porOperador, HSSFWorkbook workbook, HSSFSheet sheet,
			HSSFCellStyle styleTitulo, HSSFCellStyle style, int rownum, int cellInicio) {
		HSSFRow row;
		HSSFCell cell;
		sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, cellInicio, cellInicio + 2));
		row = sheet.createRow(rownum);
		cell = row.createCell(cellInicio);
		cell.setCellValue(new HSSFRichTextString("Resumo"));
		cell.setCellStyle(styleTitulo);
		cell = row.createCell(cellInicio + 1);
		cell.setCellStyle(style);
		cell = row.createCell(cellInicio + 2);
		cell.setCellStyle(style);

		rownum++;
		itemLegendaPorOperador(workbook, "Possibilidade de Ganho", porOperador.getPossibilidadeGanho(), sheet, rownum,
				cellInicio);
		rownum++;
		itemLegendaPorOperador(workbook, "Media de Desconto", porOperador.getMediaDesconto(), sheet, rownum,
				cellInicio);
		rownum++;
		itemLegendaPorOperador(workbook, "Ganho", porOperador.getGanho(), sheet, rownum, cellInicio);
		rownum++;
		itemLegendaPorOperador(workbook, "Valor nao Convertido", porOperador.getValorNaoConvertido(), sheet, rownum,
				cellInicio);
		rownum++;
		itemLegendaPorOperador(workbook, "%Media de nao Conversao", porOperador.getMediaNaoConversao(), sheet, rownum,
				cellInicio);
	}

	private static int itemPorUnidade(HSSFSheet sheet, HSSFCellStyle styleTitulo, HSSFCellStyle style, int rownum,
			ItemPorUnidade unidade) {
		sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 2, 6));
		HSSFRow row = sheet.createRow(rownum++);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("Data"));
		cell.setCellStyle(styleTitulo);
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("Unidade"));
		cell.setCellStyle(styleTitulo);
		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString(unidade.getUnidadeNome()));
		cell.setCellStyle(style);
		for (int i = 3; i < 7; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
		}

		row = sheet.createRow(rownum++);
		tituloPorUnidade(styleTitulo, row);

		int position = 0;
		for (ItemPorUnidadeItem obj : unidade.getItens()) {
			row = sheet.createRow(rownum++);
			if (++position % 2 == 0) {
				itemPorUnidadeItem(styleGrade1, row, obj);
			} else {
				itemPorUnidadeItem(styleGrade2, row, obj);
			}
		}

		row = sheet.createRow(rownum++);
		cell = row.createCell(0);
		cell.setCellStyle(styleTitulo);
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("Total"));
		cell.setCellStyle(styleTitulo);
		for (int i = 2; i < 7; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(styleTitulo);
		}
		rownum++;
		return rownum;
	}

	private static void itemLegendaPorOperador(HSSFWorkbook workbook, String titulo, String valor, HSSFSheet sheet,
			int rownum, int cellInicio) {

		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBold(false);
		style.setFont(font);

		HSSFCellStyle styleTitulo = workbook.createCellStyle();
		styleTitulo.setBorderTop(BorderStyle.THIN);
		styleTitulo.setBorderBottom(BorderStyle.THIN);
		styleTitulo.setBorderLeft(BorderStyle.THIN);
		styleTitulo.setBorderRight(BorderStyle.THIN);
		font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBold(true);
		styleTitulo.setFont(font);

		HSSFRow row;
		HSSFCell cell;
		sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, cellInicio, cellInicio + 1));
		row = sheet.createRow(rownum);
		cell = row.createCell(cellInicio);
		cell.setCellValue(new HSSFRichTextString(titulo));
		cell.setCellStyle(styleTitulo);

		cell = row.createCell(cellInicio + 1);
		cell.setCellStyle(style);
		cell = row.createCell(cellInicio + 2);
		cell.setCellValue(new HSSFRichTextString(valor));
		cell.setCellStyle(style);
	}

	private static void tituloPorOperador(HSSFCellStyle style, HSSFRow row) {
		HSSFCell c0 = row.createCell(0);
		c0.setCellValue("Data");
		c0.setCellStyle(style);
		HSSFCell c1 = row.createCell(1);
		c1.setCellValue(new HSSFRichTextString("Operador"));
		c1.setCellStyle(style);
		HSSFCell c2 = row.createCell(2);
		c2.setCellValue(new HSSFRichTextString("Unidade"));
		c2.setCellStyle(style);
		HSSFCell c3 = row.createCell(3);
		c3.setCellValue(new HSSFRichTextString("Exame"));
		c3.setCellStyle(style);
		HSSFCell c4 = row.createCell(4);
		c4.setCellValue(new HSSFRichTextString("Especialidade"));
		c4.setCellStyle(style);
		HSSFCell c5 = row.createCell(5);
		c5.setCellValue(new HSSFRichTextString("Valor"));
		c5.setCellStyle(style);
		HSSFCell c6 = row.createCell(6);
		c6.setCellValue(new HSSFRichTextString("%Desconto"));
		c6.setCellStyle(style);
		HSSFCell c7 = row.createCell(7);
		c7.setCellValue(new HSSFRichTextString("Valor Desconto"));
		c7.setCellStyle(style);
		HSSFCell c8 = row.createCell(8);
		c8.setCellValue(new HSSFRichTextString("Convertido"));
		c8.setCellStyle(style);
		HSSFCell c9 = row.createCell(9);
		c9.setCellValue(new HSSFRichTextString("Valor Não Convertido"));
		c9.setCellStyle(style);
	}

	private static void tituloPorEspecialidade(HSSFCellStyle style, HSSFRow row) {
		HSSFCell c0 = row.createCell(0);
		c0.setCellValue(new HSSFRichTextString("Data"));
		c0.setCellStyle(style);
		HSSFCell c4 = row.createCell(1);
		c4.setCellValue(new HSSFRichTextString("Especialidade"));
		c4.setCellStyle(style);
		HSSFCell c5 = row.createCell(2);
		c5.setCellValue(new HSSFRichTextString("Valor"));
		c5.setCellStyle(style);
		HSSFCell c6 = row.createCell(3);
		c6.setCellValue(new HSSFRichTextString("%Desconto"));
		c6.setCellStyle(style);
		HSSFCell c7 = row.createCell(4);
		c7.setCellValue(new HSSFRichTextString("Valor Desconto"));
		c7.setCellStyle(style);
		HSSFCell c8 = row.createCell(5);
		c8.setCellValue(new HSSFRichTextString("Convertido"));
		c8.setCellStyle(style);
		HSSFCell c9 = row.createCell(6);
		c9.setCellValue(new HSSFRichTextString("Valor Nao Convertido"));
		c9.setCellStyle(style);
	}

	private static void tituloPorUnidade(HSSFCellStyle style, HSSFRow row) {
		HSSFCell c0 = row.createCell(0);
		c0.setCellStyle(style);
		HSSFCell c4 = row.createCell(1);
		c4.setCellValue(new HSSFRichTextString("Especialidade"));
		c4.setCellStyle(style);
		HSSFCell c5 = row.createCell(2);
		c5.setCellValue(new HSSFRichTextString("Valor"));
		c5.setCellStyle(style);
		HSSFCell c6 = row.createCell(3);
		c6.setCellValue(new HSSFRichTextString("%Desconto"));
		c6.setCellStyle(style);
		HSSFCell c7 = row.createCell(4);
		c7.setCellValue(new HSSFRichTextString("Valor Desconto"));
		c7.setCellStyle(style);
		HSSFCell c8 = row.createCell(5);
		c8.setCellValue(new HSSFRichTextString("Convertido"));
		c8.setCellStyle(style);
		HSSFCell c9 = row.createCell(6);
		c9.setCellValue(new HSSFRichTextString("Valor Nao Convertido"));
		c9.setCellStyle(style);
	}

	private static void itemPorOperador(HSSFCellStyle style, HSSFRow row, ItemPorOperador item) {
		HSSFCell c0 = row.createCell(0);
		c0.setCellValue(new HSSFRichTextString(item.getData()));
		c0.setCellStyle(style);
		HSSFCell c1 = row.createCell(1);
		c1.setCellValue(new HSSFRichTextString(item.getUsuarioCodigo()));
		c1.setCellStyle(style);
		HSSFCell c2 = row.createCell(2);
		c2.setCellValue(new HSSFRichTextString(item.getUnidade()));
		c2.setCellStyle(style);
		HSSFCell c3 = row.createCell(3);
		c3.setCellValue(new HSSFRichTextString(item.getExame()));
		c3.setCellStyle(style);
		HSSFCell c4 = row.createCell(4);
		c4.setCellValue(new HSSFRichTextString(item.getEspecialidade()));
		c4.setCellStyle(style);
		HSSFCell c5 = row.createCell(5);
		c5.setCellValue(new HSSFRichTextString(item.getValor().toString()));
		c5.setCellStyle(style);
		HSSFCell c6 = row.createCell(6);
		c6.setCellValue(new HSSFRichTextString(item.getDesconto().toString()));
		c6.setCellStyle(style);
		HSSFCell c7 = row.createCell(7);
		c7.setCellValue(new HSSFRichTextString(item.getValorDesconto().toString()));
		c7.setCellStyle(style);
		HSSFCell c8 = row.createCell(8);
		c8.setCellValue(new HSSFRichTextString(item.getConvertido()));
		c8.setCellStyle(style);
		HSSFCell c9 = row.createCell(9);
		c9.setCellValue(new HSSFRichTextString(item.getValorNaoConvertido().toString()));
		c9.setCellStyle(style);
	}

	private static void itemPorEspecialidade(HSSFCellStyle style, HSSFRow row, ItemPorEspecialidade item) {
		HSSFCell c0 = row.createCell(0);
		c0.setCellValue(new HSSFRichTextString(item.getData()));
		c0.setCellStyle(style);
		HSSFCell c4 = row.createCell(1);
		c4.setCellValue(new HSSFRichTextString(item.getEspecialidade()));
		c4.setCellStyle(style);
		HSSFCell c5 = row.createCell(2);
		c5.setCellValue(new HSSFRichTextString(item.getValor().toString()));
		c5.setCellStyle(style);
		HSSFCell c6 = row.createCell(3);
		c6.setCellValue(new HSSFRichTextString(item.getDesconto().toString()));
		c6.setCellStyle(style);
		HSSFCell c7 = row.createCell(4);
		c7.setCellValue(new HSSFRichTextString(item.getValorDesconto().toString()));
		c7.setCellStyle(style);
		HSSFCell c8 = row.createCell(5);
		c8.setCellValue(new HSSFRichTextString(item.getConvertido()));
		c8.setCellStyle(style);
		HSSFCell c9 = row.createCell(6);
		c9.setCellValue(new HSSFRichTextString(item.getValorNaoConvertido().toString()));
		c9.setCellStyle(style);
	}

	private static void itemPorUnidadeItem(HSSFCellStyle style, HSSFRow row, ItemPorUnidadeItem item) {
		HSSFCell c0 = row.createCell(0);
		c0.setCellValue(new HSSFRichTextString(item.getData()));
		c0.setCellStyle(style);
		HSSFCell c4 = row.createCell(1);
		c4.setCellValue(new HSSFRichTextString(item.getEspecialidade()));
		c4.setCellStyle(style);
		HSSFCell c5 = row.createCell(2);
		c5.setCellValue(new HSSFRichTextString(item.getValor().toString()));
		c5.setCellStyle(style);
		HSSFCell c6 = row.createCell(3);
		c6.setCellValue(new HSSFRichTextString(item.getDesconto().toString()));
		c6.setCellStyle(style);
		HSSFCell c7 = row.createCell(4);
		c7.setCellValue(new HSSFRichTextString(item.getValorDesconto().toString()));
		c7.setCellStyle(style);
		HSSFCell c8 = row.createCell(5);
		c8.setCellValue(new HSSFRichTextString(item.getConvertido()));
		c8.setCellStyle(style);
		HSSFCell c9 = row.createCell(6);
		c9.setCellValue(new HSSFRichTextString(item.getValorNaoConvertido().toString()));
		c9.setCellStyle(style);
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		RelatorioNegociacaoService.fileName = fileName;
	}
}