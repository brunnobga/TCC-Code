/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
/**
 *
 * @author caio
 */
public class Dialog {
	public enum TipoGetFile{
		Abrir, Salvar
	}
	
	public static String getDirectory(TipoGetFile tp){
		JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showDialog(null, tp.toString());
		if(result == JFileChooser.CANCEL_OPTION){   
			return "";
		} else {   
			return fileChooser.getSelectedFile().getPath();
		}
	}
        public static String getDirectory(TipoGetFile tp, String path){
		JFileChooser fileChooser = new JFileChooser(path);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showDialog(null, tp.toString());
		if(result == JFileChooser.CANCEL_OPTION){   
			return "";
		} else {   
			return fileChooser.getSelectedFile().getPath();
		}
	}
	public static String getFile(TipoGetFile tp){
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showDialog(null, tp.toString());
		if(result == JFileChooser.CANCEL_OPTION){   
			return "";
		} else {   
			return fileChooser.getSelectedFile().getPath();
		}
	}
	public static void msgWarning(String mensagem, String titulo){
		javax.swing.JOptionPane.showMessageDialog(null, mensagem, titulo, javax.swing.JOptionPane.WARNING_MESSAGE);
	}
	public static void msgInfo(String mensagem, String titulo){
		javax.swing.JOptionPane.showMessageDialog(null,mensagem,titulo,javax.swing.JOptionPane.INFORMATION_MESSAGE);   
	}
	public static void msgError(String mensagem, String titulo){
		javax.swing.JOptionPane.showMessageDialog(null, mensagem,titulo, JOptionPane.ERROR_MESSAGE);
	}
	public static boolean question(String pergunta, String titulo){
		int n = javax.swing.JOptionPane.showConfirmDialog(null, pergunta, titulo, javax.swing.JOptionPane.YES_NO_OPTION);
		if (n == 0){
			return true;
		} 
		return false;
	}
	public static String input(String msg){
		return(String.valueOf(javax.swing.JOptionPane.showInputDialog(msg))); 
	}
}