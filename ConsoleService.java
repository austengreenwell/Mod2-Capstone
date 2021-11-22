package com.techelevator.view;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void printTransferList (Transfer[] transfers) {
		System.out.println("------------------------------------------------");
		System.out.println("Transfers");
		System.out.println("ID" + "      " + "From/To" + "      "  + "Amount");
		System.out.println("------------------------------------------------");
		System.out.println("Please enter transfer ID to view details (0 to cancel): ");
		for (Transfer transfer : transfers) {
			System.out.println(transfer.getId() + "   " + "From: " + transfer.getAccountFrom() + "To: " + transfer.getAccountTo() +
					"  " + transfer.getAmount());
		}

	}

	public void printBalance () {
//		System.out.println("Your current account balance is: $" + );

	}

	public Transfer promptForSendTransfer(){
		return promptForSendTransfer(null);
	}


	public Transfer promptForSendTransfer (Transfer existingTransfer) {
		Transfer newTransfer = null;
		while (newTransfer == null) {
			System.out.println("------------------------------");
			System.out.println();
			System.out.println("Enter the recipient account and transfer amount (separated by commas): ");
			if (existingTransfer != null) {
				System.out.println(existingTransfer);
			} else {
				System.out.println("Example: 1234, 150.00");
			}
			System.out.println("------------------------------");
			System.out.println();
			newTransfer = makeSendTransfer(in.nextLine());
			if (newTransfer == null) {
				System.out.println("Invalid entry. Please try again.");
			}
		}
		if (existingTransfer != null) {
			newTransfer.setId(existingTransfer.getId());
		}

		return newTransfer;
	}

	private Transfer makeSendTransfer(String csv) {
		Transfer transfer = null;
		String[] parsed = csv.split(",");
		if (parsed.length == 2) {
			try {
				transfer = new Transfer();
				transfer.setAccountFrom();
				transfer.setAccountTo(Long.parseLong(parsed[0].trim()));
				transfer.setAmount(BigDecimal.valueOf(Long.parseLong(parsed[1].trim()),2));
			}catch (NumberFormatException e){
				transfer = null;
			}
		}
		return transfer;
	}


	public void requestTransfer () {

	}




	public void printErrorMessage() {
		System.out.println("An error has occurred.");
	}
}
