package ti.modules.titanium.ui.widget;

import org.appcelerator.titanium.TiDict;
import org.appcelerator.titanium.TiProxy;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.Log;
import org.appcelerator.titanium.util.TiConfig;
import org.appcelerator.titanium.view.TiUIView;

import android.widget.Toast;

public class TiUINotification extends TiUIView
{
	private static final String LCAT = "TiUINotifier";
	private static final boolean DBG = TiConfig.LOGD;

	private Toast toast;

	public TiUINotification(TiViewProxy proxy) {
		super(proxy);
		if (DBG) {
			Log.d(LCAT, "Creating a notifier");
		}
		toast = Toast.makeText(proxy.getTiContext().getActivity(), "", Toast.LENGTH_SHORT);
	}

	@Override
	public void processProperties(TiDict d)
	{
		super.processProperties(d);
	}


	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, TiProxy proxy)
	{
		// we don't need any properties.
	}

	public void show(TiDict options) {
		toast.setText((String) proxy.getDynamicValue("message"));
		toast.show();
	}

	public void hide(TiDict options) {
		toast.cancel();
	}
}
