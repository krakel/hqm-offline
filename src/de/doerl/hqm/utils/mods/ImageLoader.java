package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.Utils;

public class ImageLoader extends Thread {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	public static final ImageLoader SINGLETON = new ImageLoader();
	private static HashMap<String, IHandler> sHandler = new HashMap<>();
	private static HashMap<String, Image> sCache = new HashMap<>();
	private LinkedList<Request> mQueue = new LinkedList<>();

	private ImageLoader() {
		setName( "ImageLoader");
		setPriority( NORM_PRIORITY - 1);
		setDaemon( true);
	}

	public static void addHandler( IHandler hdl) {
		sHandler.put( hdl.getName(), hdl);
	}

	public static Image getImage( AStack stk, Runnable cb) {
		if (stk != null) {
			return getImage( stk.getKey(), cb);
		}
		else {
			return null;
		}
	}

	public static Image getImage( String key, Runnable cb) {
		Image img = sCache.get( key);
		if (img == null && key != null && cb != null) {
			int p1 = key.indexOf( ':');
			if (p1 < 0) {
				Utils.log( LOGGER, Level.WARNING, "wrong stack name: {0}", key);
			}
			else {
				String mod = key.substring( 0, p1);
				String stk = key.substring( p1 + 1);
				SINGLETON.add( key, mod, stk, cb);
			}
		}
		return img;
	}

	public static void init() {
		addHandler( new AE2Handler());
		addHandler( new AE2stuffHandler());
		addHandler( new AgriCraftHandler());
		addHandler( new AutomagyHandler());
		addHandler( new AutopackagerHandler());
		addHandler( new BagginsesHandler());
		addHandler( new BiblioCraftHandler());
		addHandler( new BiblioWoodsNaturaHandler());
		addHandler( new BigReactorsHandler());
		addHandler( new BloodMagicHandler());
		addHandler( new BotaniaHandler());
		addHandler( new CompactStorageHandler());
		addHandler( new EnderTechHandler());
		addHandler( new ExAstrisHandler());
		addHandler( new ExNihiloHandler());
		addHandler( new ExtraUtilitiesHandler());
		addHandler( new ForbiddenMagicHandler());
		addHandler( new ForestryHandler());
		addHandler( new ForgeHandler());
		addHandler( new GendustryHandler());
		addHandler( new HardcoreHandler());
		addHandler( new HarvestcraftHandler());
		addHandler( new HeadCrumbsHandler());
		addHandler( new IguanaTweaksHandler());
		addHandler( new JabbaHandler());
		addHandler( new MagicBeesHandler());
		addHandler( new MineFactoryHandler());
		addHandler( new NaturaHandler());
		addHandler( new NodalMechanicsHandler());
		addHandler( new ProgressiveHandler());
		addHandler( new RainmakerHandler());
		addHandler( new RefinedRelocationHandler());
		addHandler( new RFwindmillHandler());
		addHandler( new RouterRebornHandler());
		addHandler( new SanguimancyHandler());
		addHandler( new SolarFluxHandler());
		addHandler( new StevesWorkshopHandler());
		addHandler( new StorageDrawersHandler());
		addHandler( new SuperCraftingHandler());
		addHandler( new ThaumcraftHandler());
		addHandler( new ThaumicEnergisticsHandler());
		addHandler( new ThaumicHorizonsHandler());
		addHandler( new ThermalCastingHandler());
		addHandler( new ThermalDynamicsHandler());
		addHandler( new ThermalExpansionHandler());
		addHandler( new ThermalFoundationHandler());
		addHandler( new TinkerHandler());
		addHandler( new TravellersGearHandler());
		addHandler( new WitchingGadgetsHandler());
		SINGLETON.start();
	}

	private static void readImage( Request req) throws IOException {
		if (!sCache.containsKey( req.mKey)) {
//			Utils.log( LOGGER, Level.FINEST, "load image {0}:{1}", mod, stk);
			IHandler hdl = sHandler.get( req.mMod);
			if (hdl == null) {
				Utils.log( LOGGER, Level.WARNING, "missing handler for {0}", req.mMod);
				hdl = new DummyHandler( req.mMod);
				sHandler.put( req.mMod, hdl);
			}
			Image img = hdl.load( req.mStk);
			if (img == null) {
				Utils.log( LOGGER, Level.WARNING, "missing image for {0}", req.mKey);
			}
			else {
				sCache.put( req.mKey, img);
				if (req.mCallback != null) {
					SwingUtilities.invokeLater( req.mCallback);
				}
			}
		}
	}

	private synchronized void add( String key, String mod, String stk, Runnable cb) {
//		Utils.log( LOGGER, Level.FINEST, entry);
		mQueue.addLast( new Request( key, mod, stk, cb));
		notifyAll();
	}

	private synchronized Request getNextEntry() throws InterruptedException {
		if (mQueue.isEmpty()) {
			wait( 0);
		}
		return mQueue.removeFirst();
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				sleep( 100);
				Request entry = getNextEntry();
				if (entry != null) {
					try {
						readImage( entry);
					}
					catch (IOException ex) {
					}
					catch (Exception ex) {
						Utils.logThrows( LOGGER, Level.WARNING, ex);
					}
				}
			}
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private static class Request {
		private String mKey;
		private String mMod;
		private String mStk;
		private Runnable mCallback;

		public Request( String key, String mod, String stk, Runnable cb) {
			mKey = key;
			mMod = mod;
			mStk = stk;
			mCallback = cb;
		}
	}
}
