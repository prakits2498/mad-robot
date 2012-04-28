package com.oishii.mobile;

import java.util.ArrayList;
import java.util.List;

import com.oishii.mobile.util.TextUtils;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends OishiiBaseActivity {

	private EditText title;
	private EditText fName;
	private EditText lName;
	private EditText email;
	private EditText pwd;
	private EditText repPwd;

	@Override
	protected void hookInChildViews() {
		findViewById(R.id.btnCreateAcc).setOnClickListener(createAccListener);
		title = (EditText) findViewById(R.id.field_Title);
		fName = (EditText) findViewById(R.id.fld_FirstName);
		lName = (EditText) findViewById(R.id.fld_LastName);
		email = (EditText) findViewById(R.id.fld_Email);
		pwd = (EditText) findViewById(R.id.fld_Password);
		repPwd = (EditText) findViewById(R.id.fld_RepPassword);
	}

	View.OnClickListener createAccListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			boolean hasErrors = checkForErrors();
			if (!hasErrors) {
				createNewAccount();
			}
		}
	};

	private void createNewAccount() {

	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.create_account;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.create_acc_title);
	}

	@Override
	protected int getSreenID() {
		return R.id.myacc;
	}

	private boolean checkForErrors() {
		String errors = validate();
		if (errors.length() > 0) {

			Toast toast = Toast.makeText(CreateAccount.this, errors, 8000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return true;
		}
		return false;
	}

	private String validate() {
		// Level 1 validation
		String newline = System.getProperty("line.separator");
		StringBuilder errors = new StringBuilder();
		if (!(title.getText().toString().length() > 0)) {
			errors.append(getString(R.string.error_no_title));
			errors.append(newline);
		}
		if (!(fName.getText().toString().length() > 0)) {
			errors.append(getString(R.string.error_no_fn));
			errors.append(newline);
		}
		if (!(lName.getText().toString().length() > 0)) {
			errors.append(getString(R.string.error_no_ln));
			errors.append(newline);
		}
		if (!(email.getText().toString().length() > 0)) {
			errors.append(getString(R.string.error_no_email));
			errors.append(newline);
		}
		if (!(pwd.getText().toString().length() > 0)) {
			errors.append(getString(R.string.error_no_pwd));
			errors.append(newline);
		}

		if (!(repPwd.getText().toString().length() > 0)) {
			errors.append(getString(R.string.error_no_rpwd));
			errors.append(newline);
		}
		if (errors.length() > 0)
			return errors.toString();

		//level 2 validation
		if(!TextUtils.isValidEmailAddress(email.getText().toString())){
			errors.append(getString(R.string.error_invalid_email));
		}
		return errors.toString();
	}
}
