package bg_smg;

import javax.swing.JButton;

public class Field {
	
	JButton button;
	Figure figure;
	
	Field(){
		button = new JButton();
		figure = new Figure();
	}
	
	Field(JButton button, Figure figure){
		this.button = button;
		this.figure = figure;
	}
}

