package direction.json;

public class Posisi {
	public double lat = 0;
	public double lng = 0;
	public String alamat = "";
	public String pemilik = "";
	public String jenis= "";
	public String nama = "";
	public String ket = "";
	public String arah = "";
	public double jarak = 0;
	public int id = 0;

	public Posisi(double plat, double plng, String alamat, String spemilik) {
		this.lat = plat;
		this.lng = plng;
		this.alamat = alamat;
		this.pemilik = spemilik;
	}

	public Posisi(double plat, double plng, String alamat, String spemilik,
			double jarak) {
		this.lat = plat;
		this.lng = plng;
		this.alamat = alamat;
		this.pemilik = spemilik;
		this.jarak = jarak;
	}

	public Posisi(double plat, double plng, String snama, String jenis,
			double jarak, Integer id) {
		this.lat = plat;
		this.lng = plng;
		this.nama = snama;
		this.jarak = jarak;		
		this.id = id;

	}

}

