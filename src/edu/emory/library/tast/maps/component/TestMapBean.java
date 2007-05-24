package edu.emory.library.tast.maps.component;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.util.HibernateUtil;

public class TestMapBean
{
	
	public ZoomLevel[] getZoomLevels()
	{
		return StandardMaps.getZoomLevels();
	}
	
	public ZoomLevel getMiniMapZoomLevel()
	{
		return StandardMaps.getMiniMapZoomLevel();
	}
	
	public PointOfInterest[] getCities()
	{
		
		
		Session sess = HibernateUtil.getSession();
		Transaction trans = sess.beginTransaction();
		
		List ports = Port.loadAll(sess);
		PointOfInterest points[] = new PointOfInterest[ports.size()];
		
		int i = 0;
		for (Iterator iter = ports.iterator(); iter.hasNext();)
		{
			Port port = (Port) iter.next();
			points[i++] = new PointOfInterest(
					port.getLongitude(),
					port.getLatitude(),
					new String[] {"circle-1-2"},
					port.getName(),
					port.getName());
		}
		
		trans.commit();
		sess.close();
		
		return points;
		
	}
	
	/*

	public PointOfInterest[] getCities()
	{
		return new PointOfInterest[] {
				new PointOfInterest(-59.616667, 13.1, new String[] {"circle-1-2"}, "",  "Barbados (-59.616667, 13.1)"),
				new PointOfInterest(40.73576, -15.038455, new String[] {"circle-1-2"}, "",  "Mozambique (40.73576, -15.038455)"),
				new PointOfInterest(39.183333, -6.166667, new String[] {"circle-1-2"}, "",  "Zanzibar (39.183333, -6.166667)"),
				new PointOfInterest(39.4, -8.75, new String[] {"circle-1-2"}, "",  "Kilwa (39.4, -8.75)"),
		};
	}

	public PointOfInterest[] getCities()
	{
		return new PointOfInterest[] {
				new PointOfInterest(-59.616667, 13.1, new String[] {"circle-1-2"}, "",  "Barbados (-59.616667, 13.1)"),
				new PointOfInterest(65.46, 31.54, new String[] {"circle-1-2"}, "",  "Armarah"),
				new PointOfInterest(66.76, 33.12, new String[] {"circle-1-2"}, "", "Malek Bor Kalay"),
				new PointOfInterest(65.69, 31.7, new String[] {"circle-1-2"}, "", "Mazra`"),
				new PointOfInterest(66.68, 36.86, new String[] {"circle-1-2"}, "", "Nauvared"),
				new PointOfInterest(68.5, 35.82, new String[] {"circle-1-2"}, "", "Tunddara"),
				new PointOfInterest(19.98, 41.69, new String[] {"circle-1-2"}, "", "Uraka"),
				new PointOfInterest(20.16, 42.13, new String[] {"circle-1-2"}, "", "Xathi"),
				new PointOfInterest(14.03, -9.85, new String[] {"circle-1-2"}, "", "Demba Chio"),
				new PointOfInterest(14.4, -6.6, new String[] {"circle-1-2"}, "", "Mabemba"),
				new PointOfInterest(146.23, -36.05, new String[] {"circle-1-2"}, "", "Brimin"),
				new PointOfInterest(14.72, 47.22, new String[] {"circle-1-2"}, "", "Rattenberg"),
				new PointOfInterest(11.68, 47.27, new String[] {"circle-1-2"}, "", "Rieder"),
				new PointOfInterest(150.55, -34.23, new String[] {"circle-1-2"}, "", "Couridjah"),
				new PointOfInterest(152.6, -29.15, new String[] {"circle-1-2"}, "", "Keybarbin"),
				new PointOfInterest(152.25, -28.3, new String[] {"circle-1-2"}, "", "Tannymorel"),
				new PointOfInterest(16.81, 44.73, new String[] {"circle-1-2"}, "", "Donje Bodiroze"),
				new PointOfInterest(15.91, 45.14, new String[] {"circle-1-2"}, "", "Krokinac"),
				new PointOfInterest(18.71, 43.76, new String[] {"circle-1-2"}, "", "Pleteniste"),
				new PointOfInterest(5.47, 51.27, new String[] {"circle-1-2"}, "", "Wittenberg"),
				new PointOfInterest(-0.92, 12.98, new String[] {"circle-1-2"}, "", "Oualogotenga"),
				new PointOfInterest(24.05, 41.72, new String[] {"circle-1-2"}, "", "Bozalan"),
				new PointOfInterest(25.72, 41.45, new String[] {"circle-1-2"}, "", "Khadzhi Koy"),
				new PointOfInterest(30.36, -3.66, new String[] {"circle-1-2"}, "", "Nyaruyange"),
				new PointOfInterest(-67.48, -21.72, new String[] {"circle-1-2"}, "", "Molleucueva"),
				new PointOfInterest(-60.02, -3.13, new String[] {"circle-1-2"}, "", "Aparecida"),
				new PointOfInterest(-41.8, -16.83, new String[] {"circle-1-2"}, "", "Beirao"),
				new PointOfInterest(-45.18, -4.22, new String[] {"circle-1-2"}, "", "Paulo Ramos"),
				new PointOfInterest(28.5, 53.82, new String[] {"circle-1-2"}, "", "Domovitsk"),
				new PointOfInterest(27.1, 54.65, new String[] {"circle-1-2"}, "", "Podbrzezie Pierusze"),
				new PointOfInterest(26.55, 54.83, new String[] {"circle-1-2"}, "", "Shemëtovo"),
				new PointOfInterest(14.09, -4.2, new String[] {"circle-1-2"}, "", "Kimbimi"),
				new PointOfInterest(13.77, -4, new String[] {"circle-1-2"}, "", "Nguiri"),
				new PointOfInterest(14.2, 1.96, new String[] {"circle-1-2"}, "", "Touakama"),
				new PointOfInterest(116.9, 26.57, new String[] {"circle-1-2"}, "", "Aokeng"),
				new PointOfInterest(110.55, 20.9, new String[] {"circle-1-2"}, "", "Danshui"),
				new PointOfInterest(105.46, 33.41, new String[] {"circle-1-2"}, "", "Duanjiazhuang"),
				new PointOfInterest(104.73, 29.6, new String[] {"circle-1-2"}, "", "Gaoshi"),
				new PointOfInterest(122.22, 30.08, new String[] {"circle-1-2"}, "", "Hochia"),
				new PointOfInterest(120.72, 31.07, new String[] {"circle-1-2"}, "", "Huochengcun"),
				new PointOfInterest(117.71, 31.01, new String[] {"circle-1-2"}, "", "Laohe"),
				new PointOfInterest(120.58, 28.12, new String[] {"circle-1-2"}, "", "Meiaotitsun"),
				new PointOfInterest(121.87, 39.33, new String[] {"circle-1-2"}, "", "Sekka"),
				new PointOfInterest(113.72, 35.52, new String[] {"circle-1-2"}, "", "Suncun"),
				new PointOfInterest(-74.98, 10.98, new String[] {"circle-1-2"}, "", "La Florida"),
				new PointOfInterest(15.47, 48.97, new String[] {"circle-1-2"}, "", "Nové Sady"),
				new PointOfInterest(13.98, 50.53, new String[] {"circle-1-2"}, "", "Velemín"),
				new PointOfInterest(11.07, 52.98, new String[] {"circle-1-2"}, "", "Göttien"),
				new PointOfInterest(8.08, 51.3, new String[] {"circle-1-2"}, "", "Linneperhütte"),
				new PointOfInterest(13, 48.67, new String[] {"circle-1-2"}, "", "Reisach"),
				new PointOfInterest(11.27, 55.68, new String[] {"circle-1-2"}, "", "Kaldred"),
				new PointOfInterest(8.4, 56.02, new String[] {"circle-1-2"}, "", "Lem Stationsby"),
				new PointOfInterest(10.4, 57.23, new String[] {"circle-1-2"}, "", "Prastbro"),
				new PointOfInterest(-79.18, -1.77, new String[] {"circle-1-2"}, "", "Balzapamba"),
				new PointOfInterest(-80.03, 0.43, new String[] {"circle-1-2"}, "", "Zapotal"),
				new PointOfInterest(22.02, 58.46, new String[] {"circle-1-2"}, "", "Tagamyyza"),
				new PointOfInterest(32.75, 25.87, new String[] {"circle-1-2"}, "", "Garagus"),
				new PointOfInterest(1.32, 41.17, new String[] {"circle-1-2"}, "", "Catllar"),
				new PointOfInterest(-5.05, 39.17, new String[] {"circle-1-2"}, "", "Herrera del Duque"),
				new PointOfInterest(2.98, 42.18, new String[] {"circle-1-2"}, "", "Palau de Santa Eulalia"),
				new PointOfInterest(37.37, 6.62, new String[] {"circle-1-2"}, "", "Bole"),
				new PointOfInterest(36.93, 8.2, new String[] {"circle-1-2"}, "", "Saca"),
				new PointOfInterest(23.33, 63.42, new String[] {"circle-1-2"}, "", "Kerttuankylä"),
				new PointOfInterest(20.98, 59.93, new String[] {"circle-1-2"}, "", "Österbuggo"),
				new PointOfInterest(0.33, 45.27, new String[] {"circle-1-2"}, "", "La Borie"),
				new PointOfInterest(6.55, 47.72, new String[] {"circle-1-2"}, "", "La Neuvelle"),
				new PointOfInterest(-0.5, 46.68, new String[] {"circle-1-2"}, "", "Largeasse"),
				new PointOfInterest(5.62, 45.37, new String[] {"circle-1-2"}, "", "la Thivolliere"),
				new PointOfInterest(3.05, 45.62, new String[] {"circle-1-2"}, "", "Olloix"),
				new PointOfInterest(-1.65, 5.57, new String[] {"circle-1-2"}, "", "Sarponso Near Railway Line"),
				new PointOfInterest(-9.87, 11.45, new String[] {"circle-1-2"}, "", "Fifa"),
				new PointOfInterest(-89.52, 14.7, new String[] {"circle-1-2"}, "", "Los Planes"),
				new PointOfInterest(-87.21, 13.49, new String[] {"circle-1-2"}, "", "El Sacatustal"),
				new PointOfInterest(17.25, 45.16, new String[] {"circle-1-2"}, "", "Donji Varos"),
				new PointOfInterest(-72.99, 18.31, new String[] {"circle-1-2"}, "", "Déro"),
				new PointOfInterest(18.28, 46.68, new String[] {"circle-1-2"}, "", "Fornád"),
				new PointOfInterest(16.82, 47.57, new String[] {"circle-1-2"}, "", "Kislózs"),
				new PointOfInterest(107.48, -7.32, new String[] {"circle-1-2"}, "", "Nyalindung"),
				new PointOfInterest(106.78, -6.46, new String[] {"circle-1-2"}, "", "Pasirangin"),
				new PointOfInterest(107.38, -6.82, new String[] {"circle-1-2"}, "", "Pasirangin Dua"),
				new PointOfInterest(-8.02, 51.83, new String[] {"circle-1-2"}, "", "Ballycotton"),
				new PointOfInterest(78.62, 12.23, new String[] {"circle-1-2"}, "", "Singarappettai"),
				new PointOfInterest(49.2, 36.08, new String[] {"circle-1-2"}, "", "Chandesin"),
				new PointOfInterest(46.57, 34.12, new String[] {"circle-1-2"}, "", "Darreh Ti"),
				new PointOfInterest(49.64, 34.44, new String[] {"circle-1-2"}, "", "Jownush"),
				new PointOfInterest(45.58, 34.48, new String[] {"circle-1-2"}, "", "Kharatil"),
				new PointOfInterest(47.67, 38.4, new String[] {"circle-1-2"}, "", "Kheyav"),
				new PointOfInterest(47.23, 34.75, new String[] {"circle-1-2"}, "", "Peshtgelan"),
				new PointOfInterest(50.37, 36.23, new String[] {"circle-1-2"}, "", "Shakarnab"),
				new PointOfInterest(46.35, 36.57, new String[] {"circle-1-2"}, "", "Uynehchi"),
				new PointOfInterest(-14.22, 65.03, new String[] {"circle-1-2"}, "", "Bú?areyri"),
				new PointOfInterest(125.48, 39.46, new String[] {"circle-1-2"}, "", "Sinpungni"),
				new PointOfInterest(125.28, 38.9, new String[] {"circle-1-2"}, "", "Sinsandong"),
				new PointOfInterest(128.82, 40.59, new String[] {"circle-1-2"}, "", "Unsungni"),
				new PointOfInterest(127.18, 34.6, new String[] {"circle-1-2"}, "", "Changdong"),
				new PointOfInterest(126.35, 34.63, new String[] {"circle-1-2"}, "", "Kopyongni"),
				new PointOfInterest(127.4, 37.7, new String[] {"circle-1-2"}, "", "Kungol"),
				new PointOfInterest(84.2, 48.78, new String[] {"circle-1-2"}, "", "Tuzkain"),
				new PointOfInterest(36.07, 34.4, new String[] {"circle-1-2"}, "", "Al Maqtu`ah"),
				new PointOfInterest(35.85, 34.29, new String[] {"circle-1-2"}, "", "`Ayn `Ikrin"),
				new PointOfInterest(80.35, 7.3, new String[] {"circle-1-2"}, "", "Naranbedda"),
				new PointOfInterest(-8.62, 7.4, new String[] {"circle-1-2"}, "", "Beleplo"),
				new PointOfInterest(24.69, 57.51, new String[] {"circle-1-2"}, "", "Sleikas"),
				new PointOfInterest(-4.68, 34.01, new String[] {"circle-1-2"}, "", "Ait Ben Zemour"),
				new PointOfInterest(-9.6, 31.17, new String[] {"circle-1-2"}, "", "Amsittene Hammou"),
				new PointOfInterest(-9.72, 29.77, new String[] {"circle-1-2"}, "", "Dcheira"),
				new PointOfInterest(-3.03, 35.13, new String[] {"circle-1-2"}, "", "Douar Fadrat"),
				new PointOfInterest(-7.82, 31.02, new String[] {"circle-1-2"}, "", "Timerselt"),
				new PointOfInterest(44.83, -18.13, new String[] {"circle-1-2"}, "", "Ambalamanga"),
				new PointOfInterest(46.72, -21.28, new String[] {"circle-1-2"}, "", "Antsavahory"),
				new PointOfInterest(46.92, -17.25, new String[] {"circle-1-2"}, "", "Mayotoaolana"),
				new PointOfInterest(22.73, 41.43, new String[] {"circle-1-2"}, "", "Ednokukevo"),
				new PointOfInterest(-3.28, 16.23, new String[] {"circle-1-2"}, "", "Baoua"),
				new PointOfInterest(-99.9, 17.75, new String[] {"circle-1-2"}, "", "Chapultepec"),
				new PointOfInterest(-111.66, 25.04, new String[] {"circle-1-2"}, "", "Constitución"),
				new PointOfInterest(-103.22, 19.73, new String[] {"circle-1-2"}, "", "Paso Grande"),
				new PointOfInterest(-99.9, 25.75, new String[] {"circle-1-2"}, "", "Rancho Los Elizondo"),
				new PointOfInterest(-115.37, 30.48, new String[] {"circle-1-2"}, "", "Rosarito"),
				new PointOfInterest(32.9, -16.94, new String[] {"circle-1-2"}, "", "Aldeia Drunga"),
				new PointOfInterest(35.89, -16, new String[] {"circle-1-2"}, "", "Chitambo"),
				new PointOfInterest(33.84, -21.49, new String[] {"circle-1-2"}, "", "Gueligue"),
				new PointOfInterest(10.02, 7.38, new String[] {"circle-1-2"}, "", "Awuhe"),
				new PointOfInterest(5.35, 12.85, new String[] {"circle-1-2"}, "", "Denge"),
				new PointOfInterest(5.27, 51.47, new String[] {"circle-1-2"}, "", "Oostelbeers"),
				new PointOfInterest(12.05, 66.05, new String[] {"circle-1-2"}, "", "Gasvar"),
				new PointOfInterest(58.6, 23.3, new String[] {"circle-1-2"}, "", "Madam"),
				new PointOfInterest(-80.98, 8.1, new String[] {"circle-1-2"}, "", "Santiago"),
				new PointOfInterest(143.92, -6.35, new String[] {"circle-1-2"}, "", "Kaware"),
				new PointOfInterest(142.67, -3.5, new String[] {"circle-1-2"}, "", "Ringin"),
				new PointOfInterest(123.2, 11, new String[] {"circle-1-2"}, "", "Cadiz Viejo"),
				new PointOfInterest(120.7, 13.92, new String[] {"circle-1-2"}, "", "Navotas"),
				new PointOfInterest(72.42, 34.41, new String[] {"circle-1-2"}, "", "Bagai"),
				new PointOfInterest(72.77, 33.71, new String[] {"circle-1-2"}, "", "Ban"),
				new PointOfInterest(72.43, 31.44, new String[] {"circle-1-2"}, "", "Chah Tahilpurwala"),
				new PointOfInterest(72.14, 30.48, new String[] {"circle-1-2"}, "", "Chak Nine-Eight R"),
				new PointOfInterest(74.41, 31.74, new String[] {"circle-1-2"}, "", "Gurangwala"),
				new PointOfInterest(73.15, 31.82, new String[] {"circle-1-2"}, "", "Qila Umar Shah"),
				new PointOfInterest(18.48, 50.52, new String[] {"circle-1-2"}, "", "Dabrowka"),
				new PointOfInterest(17.33, 50.93, new String[] {"circle-1-2"}, "", "Scinawa Polska"),
				new PointOfInterest(-8.42, 40.18, new String[] {"circle-1-2"}, "", "Vila Franca"),
				new PointOfInterest(24.05, 44.98, new String[] {"circle-1-2"}, "", "Grosi"),
				new PointOfInterest(20.63, 46.08, new String[] {"circle-1-2"}, "", "Sînnicolau Mare"),
				new PointOfInterest(24.5, 44.95, new String[] {"circle-1-2"}, "", "Valea Cuca"),
				new PointOfInterest(38.11, 52.16, new String[] {"circle-1-2"}, "", "Borki"),
				new PointOfInterest(31.78, 58.07, new String[] {"circle-1-2"}, "", "Bykovo"),
				new PointOfInterest(78.58, 56.63, new String[] {"circle-1-2"}, "", "Ichkalinskiy"),
				new PointOfInterest(45.99, 48.39, new String[] {"circle-1-2"}, "", "Konovalova"),
				new PointOfInterest(95.4, 51.53, new String[] {"circle-1-2"}, "", "KoptuAksy"),
				new PointOfInterest(34.05, 55.22, new String[] {"circle-1-2"}, "", "Lysovo"),
				new PointOfInterest(36.83, 52.95, new String[] {"circle-1-2"}, "", "Malinovets"),
				new PointOfInterest(30.25, 64.87, new String[] {"circle-1-2"}, "", "Maryavaara"),
				new PointOfInterest(40.3, 44.75, new String[] {"circle-1-2"}, "", "Obolduyevskiy"),
				new PointOfInterest(27.88, 57.27, new String[] {"circle-1-2"}, "", "Paltsava"),
				new PointOfInterest(42.18, 58.62, new String[] {"circle-1-2"}, "", "Pilayevo"),
				new PointOfInterest(39.17, 52.98, new String[] {"circle-1-2"}, "", "PokrovoZarechenskaya Sloboda"),
				new PointOfInterest(41.52, 44.56, new String[] {"circle-1-2"}, "", "Roznya"),
				new PointOfInterest(41.37, 61.19, new String[] {"circle-1-2"}, "", "Shenosha"),
				new PointOfInterest(35.5, 53.33, new String[] {"circle-1-2"}, "", "Sorokina"),
				new PointOfInterest(60.4, 54.57, new String[] {"circle-1-2"}, "", "Uvelka"),
				new PointOfInterest(85.28, 55.5, new String[] {"circle-1-2"}, "", "Vagino"),
				new PointOfInterest(56.39, 59.46, new String[] {"circle-1-2"}, "", "Vasilyeva"),
				new PointOfInterest(40.32, 59.98, new String[] {"circle-1-2"}, "", "Volovinskaya"),
				new PointOfInterest(33.04, 53.12, new String[] {"circle-1-2"}, "", "Yelovets"),
				new PointOfInterest(35.03, 57.05, new String[] {"circle-1-2"}, "", "Zhabkino"),
				new PointOfInterest(17.02, 60.63, new String[] {"circle-1-2"}, "", "Kusbo"),
				new PointOfInterest(18.5, 64.45, new String[] {"circle-1-2"}, "", "Rödingträsk"),
				new PointOfInterest(-12.18, 8.48, new String[] {"circle-1-2"}, "", "Mabange"),
				new PointOfInterest(-16.75, 14.53, new String[] {"circle-1-2"}, "", "Godaguene"),
				new PointOfInterest(41.77, 36.89, new String[] {"circle-1-2"}, "", "`Unayzah"),
				new PointOfInterest(21.85, 14.72, new String[] {"circle-1-2"}, "", "Ohan"),
				new PointOfInterest(1.57, 6.47, new String[] {"circle-1-2"}, "", "Momé Balémé"),
				new PointOfInterest(102.39, 17.09, new String[] {"circle-1-2"}, "", "Ban Bok"),
				new PointOfInterest(100.85, 12.75, new String[] {"circle-1-2"}, "", "Ban Klet"),
				new PointOfInterest(101.75, 6.2, new String[] {"circle-1-2"}, "", "Kampong Yaho"),
				new PointOfInterest(10.01, 36.95, new String[] {"circle-1-2"}, "", "Douar Lezdine"),
				new PointOfInterest(30.5, 40.02, new String[] {"circle-1-2"}, "", "Alpat"),
				new PointOfInterest(31.29, 38.97, new String[] {"circle-1-2"}, "", "Demircili"),
				new PointOfInterest(26.7, 41.33, new String[] {"circle-1-2"}, "", "Hamitli"),
				new PointOfInterest(38.28, 37.76, new String[] {"circle-1-2"}, "", "Hüsnümansur"),
				new PointOfInterest(31.37, 38.08, new String[] {"circle-1-2"}, "", "Karaagac"),
				new PointOfInterest(33.7, 37.95, new String[] {"circle-1-2"}, "", "Kozin YaylasÄ±"),
				new PointOfInterest(27.87, 41.08, new String[] {"circle-1-2"}, "", "Sehbaz"),
				new PointOfInterest(121.6, 24.97, new String[] {"circle-1-2"}, "", "Laoliao"),
				new PointOfInterest(29.32, 48.92, new String[] {"circle-1-2"}, "", "Kryshtopovka"),
				new PointOfInterest(24.3, 48.77, new String[] {"circle-1-2"}, "", "Mezhgorye"),
				new PointOfInterest(25.93, 48.15, new String[] {"circle-1-2"}, "", "Rakivskiy"),
				new PointOfInterest(37.12, 49.03, new String[] {"circle-1-2"}, "", "Vernopolye"),
				new PointOfInterest(-87.05, 33.67, new String[] {"circle-1-2"}, "", "Media"),
				new PointOfInterest(-83.6, 37.01, new String[] {"circle-1-2"}, "", "Ashers Fork"),
				new PointOfInterest(-90.86, 29.61, new String[] {"circle-1-2"}, "", "Humphreys"),
				new PointOfInterest(-69.04, 47.08, new String[] {"circle-1-2"}, "", "Allagash"),
				new PointOfInterest(-84.18, 39.53, new String[] {"circle-1-2"}, "", "Ridgeville"),
				new PointOfInterest(-75.96, 40.39, new String[] {"circle-1-2"}, "", "River View Park"),
				new PointOfInterest(-87.83, 36.16, new String[] {"circle-1-2"}, "", "Trinity"),
				new PointOfInterest(-67.65, 8.73, new String[] {"circle-1-2"}, "", "Santa Teresa"),
				new PointOfInterest(106.65, 22.82, new String[] {"circle-1-2"}, "", "Cnong Hu"),
				new PointOfInterest(105.97, 22.25, new String[] {"circle-1-2"}, "", "Done Dan"),
				new PointOfInterest(106.23, 20.63, new String[] {"circle-1-2"}, "", "'n Nong'"),
				new PointOfInterest(109.03, 13.53, new String[] {"circle-1-2"}, "", "Muc Thinh"),
				new PointOfInterest(46.8, 14.63, new String[] {"circle-1-2"}, "", "Al-Kuraibiyah"),
				new PointOfInterest(43.38, 14.76, new String[] {"circle-1-2"}, "", "Qaryat al Battarah"),
				
		};
	}
	 */

}
