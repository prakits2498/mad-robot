package com.oishii.mobile;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Locations extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.myacc;
	}

	@Override
	protected void hookInChildViews() {
		Button btnArbit = getArbitartButton();
		btnArbit.setBackgroundResource(R.drawable.btn_title_selector);
		btnArbit.setOnClickListener(addLocation);
	}

	private View.OnClickListener addLocation = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Dialog errorDialog = new Dialog(Locations.this);
			errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			errorDialog.setContentView(R.layout.add_location);
			errorDialog.setTitle(R.string.btn_add);
			company = (EditText) errorDialog.findViewById(R.id.fld_comp);
			floor = (EditText)errorDialog. findViewById(R.id.fld_floor);
			address = (EditText) errorDialog.findViewById(R.id.fld_address);
			city = (EditText)errorDialog. findViewById(R.id.fld_city);
			postCode = (EditText)errorDialog. findViewById(R.id.fld_postcode);
			mobile = (EditText)errorDialog. findViewById(R.id.fld_mobile);
			
			errorDialog.findViewById(R.id.radioBill).setOnClickListener(
					billShipListener);
			errorDialog.findViewById(R.id.radioShip).setOnClickListener(
					billShipListener);
			errorDialog.findViewById(R.id.btnAddLocation).setOnClickListener(
					addLocatioOnClickListener);
			errorDialog.show();

		}
	};
	int shipping;
	int billing;

	private void resetBillingShipp() {
		shipping = billing = 0;
	}

	View.OnClickListener billShipListener = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			String tag = (String) arg0.getTag();
			if (tag.equals("S")) {
				shipping = 1;
			} else if (tag.equals("B")) {
				billing = 1;
			}
		}
	};

	View.OnClickListener addLocatioOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (isValid()) {
				executeAddLocation();
			}
		}
	};

	private EditText company;
	private EditText floor;
	private EditText address;
	private EditText city;
	private EditText postCode;
	private EditText mobile;

	private boolean isValid() {
		EditText[] text = new EditText[] { company, floor, address, city,
				postCode, mobile };
		String newline = System.getProperty("line.separator");
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < text.length; i++) {
			if(!hasValidText(text[i])){
				errors.append("* Please enter your ");
				errors.append(text[i].getTag());
				errors.append(newline);
			}
		}
		if (errors.length() > 0) {
			Toast t = Toast.makeText(getApplicationContext(), errors, 4000);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return false;
		}
		return true;
	}

	private void executeAddLocation() {

	}

	@Override
	protected int getSreenID() {
		return R.layout.locations;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.locations;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_locations);
	}

}
