package ti.modules.titanium.ui.widget;

import org.appcelerator.titanium.TiDict;
import org.appcelerator.titanium.TiProxy;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.Log;
import org.appcelerator.titanium.util.TiConfig;
import org.appcelerator.titanium.view.TiUIView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class TiUIDialog extends TiUIView
{
	private static final String LCAT = "TiUIButton";
	private static final boolean DBG = TiConfig.LOGD;

	protected Builder builder;
	protected AlertDialog dialog;

	protected class ClickHandler implements DialogInterface.OnClickListener {

		private int result;

		public ClickHandler(int id) {
			this.result = id;
		}
		public void onClick(DialogInterface dialog, int which) {
			handleEvent(result);
			dialog.dismiss();
		}
	}

	public TiUIDialog(TiViewProxy proxy) {
		super(proxy);
		if (DBG) {
			Log.d(LCAT, "Creating a dialog");
		}

		this.builder = new AlertDialog.Builder(proxy.getContext());
		this.builder.setCancelable(true);
	}

	@Override
	public void processProperties(TiDict d)
	{
		if (d.containsKey("title")) {
			builder.setTitle(d.getString("title"));
		}
		if (d.containsKey("message")) {
			builder.setMessage(d.getString("message"));
		}
		if (d.containsKey("buttons"))
		{
			String[] buttonText = d.getStringArray("buttons");
			processButtons(buttonText);
		}
		if (d.containsKey("options")) {
			String[] optionText = d.getStringArray("options");
			processOptions(optionText);
		}

		super.processProperties(d);
	}

	private void processOptions(String[] optionText) {
		builder.setSingleChoiceItems(optionText, -1 , new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				handleEvent(which);
				dialog.dismiss();
			}});
	}

	private void processButtons(String[] buttonText)
	{
		builder.setPositiveButton(null, null);
		builder.setNegativeButton(null, null);
		builder.setNeutralButton(null, null);

		for (int id = 0; id < buttonText.length; id++) {
			String text = buttonText[id];
			ClickHandler clicker = new ClickHandler(id);
			switch (id) {
			case 0:
				builder.setPositiveButton(text, clicker);
				break;
			case 1:
				builder.setNeutralButton(text, clicker);
				break;
			case 2:
				builder.setNegativeButton(text, clicker);
				break;
			default:
				Log.e(LCAT, "Only 3 buttons are supported");
			}
		}
	}


	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, TiProxy proxy)
	{
		if (DBG) {
			Log.d(LCAT, "Property: " + key + " old: " + oldValue + " new: " + newValue);
		}

		if (key.equals("title")) {
			if (dialog != null) {
				dialog.setTitle((String) newValue);
			} else {
				builder.setTitle((String) newValue);
			}
		} else if (key.equals("message")) {
			if (dialog != null) {
				dialog.setMessage((String) newValue);
			} else {
				builder.setMessage((String) newValue);
			}
		} else if (key.equals("buttons")) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

			processButtons((String[]) newValue);
		} else if (key.equals("options")) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

			processOptions((String[]) newValue);
		} else {
			super.propertyChanged(key, oldValue, newValue, proxy);
		}
	}

	public void show(TiDict options)
	{
		if (dialog == null) {
			dialog = builder.create();
		}
		dialog.show();
	}

	public void hide(TiDict options)
	{
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public void handleEvent(int id)
	{
		TiDict data = new TiDict();
		data.put("index", id);
		proxy.fireEvent("click", data);
	}
}
