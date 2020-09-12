import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
*
* @author = Ahmad Fathanah M.Adil
*
*/

class jualan {
    private String kategori;
    private String namaBarang;
    private int hargaSatuan;
    private ArrayList<String> warna;
    private ArrayList<String> ukuran;

    jualan(String kat, String namaBarang, int hargaSatuan, ArrayList<String> warna, ArrayList<String> ukuran) {
        this.kategori = kat;
        this.namaBarang = namaBarang;
        this.hargaSatuan = hargaSatuan;
        this.warna = warna;
        this.ukuran = ukuran;
    }

    void show() throws IOException {
        Scanner in = new Scanner(System.in);

        pendataanJualan.clrscr();
        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println("Kategori     : " + this.kategori);
        System.out.println("Nama Barang  : " + this.namaBarang);
        System.out.printf ("Harga Satuan : Rp.%,d\n",this.hargaSatuan);
        int nomor = 0;
        for (String i : this.warna) {
            nomor++;
            if (nomor == 1) {
                System.out.printf("Warna        : %d. %s\n", nomor, i);
            } else {
                System.out.printf("%16d. %s \n", nomor,i);
            }
        }

        int nomor2 = 0;
        for (String i : this.ukuran) {
            nomor2++;
            if (nomor2 == 1) {
                System.out.printf("Ukuran       : %d. %s\n", nomor2, i);
            } else {
                System.out.printf("%16d. %s \n", nomor2,i);
            }
        }

        String istambah = "";
        boolean isExist;
        do {

            System.out.print("Tambahkan Jualan ? (y/n) : ");
            istambah = in.next();
            if (istambah.equalsIgnoreCase("y")) {
                isExist = checkBarangIfExist(this.namaBarang);
                pendataanJualan.clrscr();
                if (isExist) {
                    System.out.println("Failed, Barang Sudah Terdapat Dalam Database !");
                } else {
                    MasukkanJualan();
                    System.out.println("Barang Berhasil Ditambahkan !");
                }
                pendataanJualan.start();
                return;
            } else if (istambah.equalsIgnoreCase("n")) {
                pendataanJualan.clrscr();
                System.out.println("Barang Batal Ditambahkan");
                pendataanJualan.start();
                return;
            } else {
                System.out.println("Warning : Masukkan Inputan Yang valid !");
            }
        } while (!istambah.equalsIgnoreCase("y") || !istambah.equalsIgnoreCase("n"));

    }

    private void MasukkanJualan () throws IOException {

        String kategori = this.kategori;
        String kategoriTanpaSpasi = kategori.replaceAll(" ", "");
        String fileJualan = kategoriTanpaSpasi + ".txt";

        File file = new File(fileJualan);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter filer = new FileWriter(file, true);
        BufferedWriter masukkkan = new BufferedWriter(filer);

        masukkkan.write(this.kategori + "_" + this.namaBarang + "_" + this.hargaSatuan + "_");
        for (int i = 0; i < this.warna.size(); i++) {
            masukkkan.write(this.warna.get(i));
            if (i != this.warna.size()-1) {
                masukkkan.write(",");
            }
        }
        masukkkan.write("_");
        for (int i = 0; i < this.ukuran.size(); i++) {
            masukkkan.write(this.ukuran.get(i));
            if (i != this.ukuran.size()-1) {
                masukkkan.write(",");
            }
        }

        masukkkan.newLine();
        masukkkan.flush();

        filer.close();
        masukkkan.close();
    }

    private boolean checkBarangIfExist(String namaBarang) throws IOException {
        boolean isExist = false;

        String kategori = this.kategori;
        String kategoriTanpaSpasi = kategori.replaceAll(" ", "");
        String fileJualan = kategoriTanpaSpasi + ".txt";

        File file = new File(fileJualan);
        FileReader filer = new FileReader(file);
        BufferedReader readfile = new BufferedReader(filer);
        String data = readfile.readLine();

        while (data != null) {

            Scanner datascan = new Scanner(data);
            datascan.useDelimiter("_");
            datascan.next();
            String namabarang_dalamFile = datascan.next();
            if (namabarang_dalamFile.equalsIgnoreCase(namaBarang)) {
                isExist = true;
            }

            data = readfile.readLine();
        }

        filer.close();
        readfile.close();

        return isExist;
    }

}

class ReadJualan {
    private String kategori;
    private boolean readall;
    public boolean FilejualanTerisi;
    public String aksi;
    public int jumlahjualan = 0;

    ReadJualan (String kategori, boolean readAll, String aksi) throws IOException {
        this.kategori = kategori;
        this.readall = readAll;
        this.aksi = aksi;
    }

    boolean tampilkan()throws IOException {
        Scanner in = new Scanner(System.in);
        boolean isFilled = false;

        if (!this.readall) {
            String kategoriTanpaSpasi = this.kategori.replaceAll(" ", "");
            String filejualan = kategoriTanpaSpasi + ".txt";
            File file = new File(filejualan);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileReader filer = new FileReader(file);
            BufferedReader readfile = new BufferedReader(filer);
            String data = readfile.readLine();

            if (data == null) {
                filer.close();
                readfile.close();
                this.FilejualanTerisi = false;
                pendataanJualan.clrscr();
                System.out.println("Jualan Anda Pada Kategori " + this.kategori +" Kosong !");

                if (this.aksi.equalsIgnoreCase("READ")) {
                    pendataanJualan.tampilkanJualan();
                    return isFilled;
                } else if (this.aksi.equalsIgnoreCase("UPDATE")) {
                    pendataanJualan.updateJualan();
                    return isFilled;
                }
            } else {
                Scanner datascan;
                int nomor = 0;
                int jumlahJualan = 0;

                this.FilejualanTerisi = true;

                pendataanJualan.clrscr();
                System.out.println("======================================================");
                System.out.println("=================== AhmadFMA.Tech ====================");
                System.out.println("======================================================");

                while (data != null) {

                    datascan = new Scanner(data);
                    datascan.useDelimiter("_");
                    String kategorijualan = datascan.next();
                    String nama = datascan.next();
                    int harga = datascan.nextInt();
                    String warna = datascan.next();
                    String ukuran = datascan.next();

                    if (nomor == 0) {
                        System.out.println("\tKategori : " + kategorijualan);
                    }

                    nomor++;
                    System.out.println("" + nomor + ".)");
                    System.out.println("     Nama barang : " + nama);
                    System.out.printf ("     Harga       : Rp.%,d\n", harga);
                    System.out.println("     Warna       : " + warna);
                    System.out.println("     Ukuran      : " + ukuran);
                    System.out.println();

                    jumlahJualan++;
                    this.jumlahjualan++;
                    isFilled = true;
                    data = readfile.readLine();
                }
                System.out.println("======================================================");
                System.out.println("Jumlah Jualan : " + jumlahJualan);

                filer.close();
                readfile.close();

            }

        }

        else  {
            String[] jenisjualan = {
                    "Jaket",
                    "Kemeja",
                    "PakaianPria",
                    "PakaianWanita",
                    "Sepatu",
                    "TasWanita",
                    "TasPria",
                    "Jaz",
                    "JamTangan"
            };

            pendataanJualan.clrscr();
            System.out.println("======================================================");
            System.out.println("=================== AhmadFMA.Tech ====================");
            System.out.println("======================================================");
            int jumlahTotalJualan = 0;
            boolean checkfile_if_filled = false;

            for (int i = 0; i < jenisjualan.length; i++) {

                String kategoriJualan = jenisjualan[i];
                String fileJualan = kategoriJualan+".txt";

                File file = new File(fileJualan);

                if (!file.exists()) {
                    file.createNewFile();
                }
                FileReader filer = new FileReader(file);
                BufferedReader readfile = new BufferedReader(filer);
                String data = readfile.readLine();

                if (data == null) {
                    isFilled = false;
                    continue;
                }

                Scanner datascan;
                int nomor = 0;

                while (data != null) {
                    checkfile_if_filled = true;
                    datascan = new Scanner(data);
                    datascan.useDelimiter("_");
                    String kategorijualan = datascan.next();
                    String nama = datascan.next();
                    int harga = datascan.nextInt();
                    String warna = datascan.next();
                    String ukuran = datascan.next();

                    if (nomor == 0) {
                        System.out.println("\tKategori : " + kategorijualan);
                    }

                    nomor++;
                    System.out.println("" + nomor + ".)");
                    System.out.println("     Nama barang : " + nama);
                    System.out.printf ("     Harga       : Rp.%,d\n", harga);
                    System.out.println("     Warna       : " + warna);
                    System.out.println("     Ukuran      : " + ukuran);
                    System.out.println("");

                    jumlahTotalJualan++;
                    isFilled = true;
                    data = readfile.readLine();
                }

                if (i != jenisjualan.length) {
                    System.out.println("------------------------------------------------------");
                }

                filer.close();
                readfile.close();
            }

            if (!checkfile_if_filled) {
                System.out.println("Anda Belum Mendaftarkan Barang Apapun !");
                System.out.println("Total Jualan Anda : " + jumlahTotalJualan);
            } else {
                System.out.println("======================================================");
                System.out.println("Total Jualan Anda : " + jumlahTotalJualan);
            }


        }

        return isFilled;
    }

}

public class pendataanJualan {

    static final int CREATE = 1;
    static final int READ = 2;
    static final int UPDATE = 3;
    static final int DELETE = 4;
    static final int SEARCH = 5;
    static final int EXIT = 6;
    static LocalDateTime localDateTime = LocalDateTime.now();
    static DateTimeFormatter formattertanggal = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    static DateTimeFormatter formatterjam = DateTimeFormatter.ofPattern("HH:mm");
    static String tanggal = localDateTime.format(formattertanggal);
    static String jam = localDateTime.format(formatterjam);

    public static void main(String[] args) throws IOException {
        clrscr();
        start();
    }

    public static void Tampilan_utama() {
        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println("\t1. Daftarkan Jualan ");
        System.out.println("\t2. Tampilkan Jualan");
        System.out.println("\t3. Edit Jualan");
        System.out.println("\t4. Hapus Jualan");
        System.out.println("\t5. Cari Jualan");
        System.out.println("\t6. Exit");
        System.out.println("------------------------------------------------------");
    }

    public static void start() throws IOException{
        Scanner input = new Scanner(System.in);
        int pilihan = 0;

        Tampilan_utama();
        System.out.print  ("Masukkan pilihan Anda : ");
        try {
            pilihan = input.nextInt();
        } catch (InputMismatchException e) {
            clrscr();
            System.out.println("Warning : Inputan Tidak Valid !");
            start();
            return;
        }

        switch (pilihan) {
            case CREATE:
                clrscr();
                Create_Jualan();
                break;

            case READ:
                clrscr();
                tampilkanJualan();
                break;

            case UPDATE:
                clrscr();
                updateJualan();
                break;

            case DELETE:
                clrscr();
                deleteJualan();
                break;

            case SEARCH:
                searchJualan();
                break;

            case EXIT:
                exit();
                break;

            default:
                clrscr();
                System.out.println("Warning : Pilihan Tidak Tersedia !");
                start();
                break;
        }

    }

    //CREATE
    public static void Create_Jualan() throws IOException {
        Scanner input = new Scanner(System.in);
        ArrayList<String> listwarna = new ArrayList<String>();
        ArrayList<String> listUkuran = new ArrayList<>();

        //content

        String[] jenisjualan = { "kosong",
                "Jaket",
                "Kemeja",
                "Pakaian Pria",
                "Pakaian Wanita",
                "Sepatu",
                "Tas Wanita",
                "Tas Pria",
                "Jaz",
                "Jam Tangan"
        };

        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println("Kategori Yang Tersedia : ");
        for (int i = 0; i < jenisjualan.length; i++) {
            if (i == 0) {
                continue;
            }

            if (i % 2 != 0) {
                System.out.printf("\t%d. %-20s", i, jenisjualan[i]);
            }

            if (i % 2 == 0) {
                System.out.printf("%d. %s\n", i, jenisjualan[i]);
            }
        }
        System.out.println("\n------------------------------------------------------");
        System.out.println("Opsi Lain : ");
        System.out.println("\tw. Back");
        System.out.println("------------------------------------------------------");
        System.out.print  ("Pilih Kategori : ");
        String pilihan = input.next();
        String kategori = "";

        input = new Scanner(System.in);
        if (pilihan.equals("w")) {
            clrscr();
            start();
            return;
        } else {
            try {

                int index = Integer.parseInt(pilihan);
                if (index <= 0 || index > jenisjualan.length-1) {
                    clrscr();
                    System.out.println("Warning : Pilihan Tidak Tersedia !");
                    Create_Jualan();
                    return;
                } else {
                    kategori = jenisjualan[index];
                }


            } catch (NumberFormatException e) {
                clrscr();
                System.out.println("Warning : Pilihan Tidak Tersedia !");
                Create_Jualan();
                return;
            }

        }

        System.out.println("Kategori             : " + kategori);
        System.out.print  ("Masukkan Nama Barang : ");
        String nama = input.nextLine();
        int hargaSatuan = getHarga();

        clrscr();
        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println("Kategori     : " + kategori);
        System.out.println("Nama Barang  : " + nama);
        System.out.printf ("Harga Satuan : Rp.%,d\n", hargaSatuan);
        System.out.println("------------------------------------------------------");

        listwarna = getwarna();

        System.out.println("------------------------------------------------------");
        listUkuran = getUkuran();

        jualan barang = new jualan(kategori, nama, hargaSatuan, listwarna, listUkuran);
        barang.show();

    }

    private static ArrayList<String> getwarna() {
        Scanner in = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<String>();

        LANJUT:
        while (true) {
            in = new Scanner(System.in);
            System.out.print("Masukkan Warna yang Tersedia : ");
            String warna = in.nextLine();
            if (list.contains(warna)) {
                System.out.println("Warning : Warna Sudah Dimasukkan Sebelumnya !");
            } else  {
                list.add(warna);
            }

            String lanjut;
            do {
                System.out.print("Masukkan Warna Lagi ? (y/n) : ");
                lanjut = in.next();
                if (lanjut.equalsIgnoreCase("y")) {
                    continue LANJUT;
                } else if (lanjut.equalsIgnoreCase("n")) {
                    break LANJUT;
                }else  {
                    System.out.println("Warning : Masukkan Inputan yang valid !");
                }
            } while (!lanjut.equalsIgnoreCase("y") || !lanjut.equalsIgnoreCase("n"));

        }

        return list;
    }

    private static ArrayList<String> getUkuran() {
        Scanner in = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<String>();

        LANJUT:
        while (true) {
            System.out.print ("Apakah Ada Ukuran ? (y/n) : ");
            String aksi = in.next();

            if (aksi.equalsIgnoreCase("y")) {
                LABEL:
                do {
                    in = new Scanner(System.in);
                    System.out.print("Masukkan Ukuran yang Tersedia : ");
                    String ukuran = in.nextLine();
                    if (list.contains(ukuran)) {
                        System.out.println("Warning : Ukuran Sudah Dimasukkan Sebelumnya !");
                    } else  {
                        list.add(ukuran.toUpperCase());
                    }

                    String lanjut;
                    do {
                        System.out.print("Masukkan Ukuran Lagi ? (y/n) : ");
                        lanjut = in.next();
                        if (lanjut.equalsIgnoreCase("y")) {
                            continue LABEL;
                        } else if (lanjut.equalsIgnoreCase("n")) {
                            break LANJUT;
                        }else  {
                            System.out.println("Warning : Masukkan Inputan yang valid !");
                        }
                    } while (!lanjut.equalsIgnoreCase("y") || !lanjut.equalsIgnoreCase("n"));
                } while (true);

            } else if (aksi.equalsIgnoreCase("n")){
                list.add("All Size");
                break;
            } else {
                System.out.println("Warning : Inputan Tidak Valid !");
            }

        }

        return list;
    }

    private static int getHarga () {
        int harga = 0;
        Scanner in = new Scanner(System.in);
        try {
            System.out.print("Masukkan harga satuan : ");
            harga = in.nextInt();
            if (harga == 0) {
                System.out.println("Warning : harga Yang Dimasukkan Rp.0 !");
                harga = getHarga();
            }
        } catch (InputMismatchException e) {
            System.out.println("Warning : Masukkan Harga Yang Valid");
            harga = getHarga();
        }

        return harga;
    }

    //READ
    public static void tampilkanJualan() throws IOException {
        Scanner in = new Scanner(System.in);

        String[] jenisjualan = { "kosong",
                "Jaket",
                "Kemeja",
                "Pakaian Pria",
                "Pakaian Wanita",
                "Sepatu",
                "Tas Wanita",
                "Tas Pria",
                "Jaz",
                "Jam Tangan"
        };

        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println("Kategori Yang Tersedia : ");
        for (int i = 0; i < jenisjualan.length; i++) {
            if (i == 0) {
                continue;
            }

            if (i % 2 != 0) {
                System.out.printf("\t%d. %-20s", i, jenisjualan[i]);
            }

            if (i % 2 == 0) {
                System.out.printf("%d. %s\n", i, jenisjualan[i]);
            }
        }

        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("Opsi Lain : ");
        System.out.println("\tr. Tampilkan Semua");
        System.out.println("\tq. Back");
        System.out.println("\te. Exit");
        System.out.println("------------------------------------------------------");
        System.out.print  ("Pilih Kategori Jualan Yang ingin Ditampilkan : ");
        try {
            String pilihan = in.next();
            if (pilihan.equalsIgnoreCase("q")) {
                clrscr();
                start();
            }
            else if (pilihan.equalsIgnoreCase("e")) {
                exit();
                return;
            }
            else if (pilihan.equalsIgnoreCase("r")) {
                ReadJualan readJualan = new ReadJualan("", true, "READ");
                readJualan.tampilkan();
                String pilihan2;
                System.out.println("======================================================");
                System.out.println("Opsi : ");
                System.out.println("\tq. Back");
                System.out.println("\tw. Home");
                System.out.println("\te. Exit");
                System.out.println("------------------------------------------------------");
                do {
                    System.out.print  ("Pilih : ");
                    pilihan2 = in.next();
                    if (pilihan2.equalsIgnoreCase("q")) {
                        clrscr();
                        tampilkanJualan();
                        return;
                    } else if (pilihan2.equalsIgnoreCase("w")) {
                        clrscr();
                        start();
                        return;
                    } else if (pilihan2.equalsIgnoreCase("e")) {
                        exit();
                        return;
                    }
                    else {
                        System.out.println("Warning : Inputan Tidak Valid !");
                    }

                } while (!pilihan2.equalsIgnoreCase("q") || !pilihan2.equalsIgnoreCase("w"));

            }
            else {
                int index = Integer.parseInt(pilihan);
                if (index == 0 || index > jenisjualan.length-1) {
                    clrscr();
                    System.out.println("Warning : Pilihan Tidak Tersedia !");
                    tampilkanJualan();
                } else {
                    String kategori = jenisjualan[index];
                    ReadJualan readJualan = new ReadJualan(kategori, false, "READ");
                    boolean isFilled = readJualan.tampilkan();
                    if (isFilled) {
                        String pilihan2;
                        System.out.println("======================================================");
                        System.out.println("Opsi : ");
                        System.out.println("\tq. Back");
                        System.out.println("\tw. Home");
                        System.out.println("\te. Exit");
                        System.out.println("------------------------------------------------------");
                        do {
                            System.out.print  ("Pilih : ");
                            pilihan2 = in.next();
                            if (pilihan2.equalsIgnoreCase("q")) {
                                clrscr();
                                tampilkanJualan();
                                return;
                            } else if (pilihan2.equalsIgnoreCase("w")) {
                                clrscr();
                                start();
                                return;
                            } else if (pilihan2.equalsIgnoreCase("e")) {
                                exit();
                                return;
                            }
                            else {
                                System.out.println("Warning : Inputan Tidak Valid !");
                            }

                        } while (!pilihan2.equalsIgnoreCase("q") || !pilihan2.equalsIgnoreCase("w"));
                    }

                }

            }
        }catch (NumberFormatException e) {
            clrscr();
            System.out.println("Warning : Inputan Tidak Valid !");
            tampilkanJualan();
        }

    }

    //UPDATE
    public static void updateJualan() throws IOException{
        Scanner in = new Scanner(System.in);

        String[] jenisjualan = { "kosong",
                "Jaket",
                "Kemeja",
                "Pakaian Pria",
                "Pakaian Wanita",
                "Sepatu",
                "Tas Wanita",
                "Tas Pria",
                "Jaz",
                "Jam Tangan"
        };

        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println("Kategori Yang Tersedia : ");
        for (int i = 0; i < jenisjualan.length; i++) {
            if (i == 0) {
                continue;
            }

            if (i % 2 != 0) {
                System.out.printf("\t%d. %-20s", i, jenisjualan[i]);
            }

            if (i % 2 == 0) {
                System.out.printf("%d. %s\n", i, jenisjualan[i]);
            }
        }
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("Opsi Lain : ");
        System.out.println("\tq. Back");
        System.out.println("\te. Exit");
        System.out.println("------------------------------------------------------");
        System.out.print  ("Masukkan Kategori Jualan Yang Ingin Diedit : ");
        String pilihan = in.next();
        if (pilihan.equalsIgnoreCase("q")) {
            clrscr();
            start();
            return;
        } else if (pilihan.equalsIgnoreCase("e")) {
            exit();
            return;
        } else {
            int index = Integer.parseInt(pilihan);

            if (index == 0 || index > jenisjualan.length-1) {
                clrscr();
                System.out.println("Warning : Pilihan Tidak Tersedia !");
                updateJualan();
            } else {
                String kategori = jenisjualan[index];
                ReadJualan checkFileJikaAda = new ReadJualan(kategori, false, "UPDATE");
                checkFileJikaAda.tampilkan();

                if (checkFileJikaAda.FilejualanTerisi) {
                    String kategori_TanpaSpasi = kategori.replaceAll(" ", "");
                    String fileJualan = kategori_TanpaSpasi + ".txt";
                    int limit = checkFileJikaAda.jumlahjualan;
                    System.out.println("------------------------------------------------------");
                    int editNum = getNum("Masukkan No.Jualan Yang Ingin Diedit : ",limit, false);

                    File file = new File(fileJualan);
                    File tempFile = new File("TempFile.txt");

                    FileReader filer = new FileReader(file);
                    BufferedReader readfile = new BufferedReader(filer);

                    FileWriter filew = new FileWriter(tempFile);
                    BufferedWriter writeKeTemp = new BufferedWriter(filew);

                    String data = readfile.readLine();
                    Scanner datascan;

                    int entryPoint = 0;
                    while (data != null) {
                        entryPoint++;

                        if (entryPoint == editNum) {

                            datascan = new Scanner(data);
                            datascan.useDelimiter("_");
                            String Kategori = datascan.next();
                            String nama = datascan.next();
                            int harga = datascan.nextInt();
                            String warna = datascan.next();
                            String ukuran = datascan.next();

                            clrscr();
                            System.out.println("======================================================");
                            System.out.println("=================== AhmadFMA.Tech ====================");
                            System.out.println("======================================================");
                            System.out.println("Jualan Yang Ingin Diedit : ");
                            System.out.println("Nomor : " + editNum);
                            System.out.println("\tKategori     : " + Kategori);
                            System.out.println("\tNama Barang  : " + nama);
                            System.out.printf ("\tHarga Barang : Rp.%,d\n", harga);
                            System.out.println("\tWarna        : " + warna);
                            System.out.println("\tUkuran       : " + ukuran);
                            System.out.println("------------------------------------------------------");

                            String aksi;
                            int noEdit = 0;

                            isEDIT:
                            do {
                                System.out.print  ("Yakin Ingin Edit ? (y/n) : ");
                                aksi = in.next();
                                if (aksi.equalsIgnoreCase("y")) {
                                    clrscr();

                                    do {
                                        in = new Scanner(System.in);
                                        System.out.println("======================================================");
                                        System.out.println("=================== AhmadFMA.Tech ====================");
                                        System.out.println("======================================================");
                                        System.out.println("Kategori     : " + kategori);
                                        System.out.println("\t1. Nama Barang  : " + nama);
                                        System.out.printf ("\t2. Harga Barang : Rp.%,d\n", harga);
                                        System.out.println("\t3. Warna        : " + warna);
                                        System.out.println("\t4. Ukuran       : " + ukuran);
                                        System.out.println("0. Batal");
                                        System.out.println("------------------------------------------------------");
                                        try {
                                            System.out.print  ("Masukkan No.data yang Ingin Diedit : ");
                                            noEdit = in.nextInt();
                                            System.out.println("------------------------------------------------------");
                                            if (noEdit == 1) {
                                                in = new Scanner(System.in);
                                                System.out.println("Nama Sebelumnya    : " + nama);
                                                System.out.print  ("Masukkan Nama Baru : ");
                                                String namabaru = in.nextLine();
                                                writeKeTemp.write(kategori+"_"+namabaru+"_"+harga+"_"+warna+"_"+ukuran);
                                                writeKeTemp.newLine();
                                                writeKeTemp.flush();
                                                clrscr();
                                                System.out.println("Edit Nama Jualan No." + editNum + " Kategori " + kategori + " Berhasil");
                                                break isEDIT;
                                            }else if (noEdit == 2) {
                                                in = new Scanner(System.in);
                                                System.out.printf ("Harga Sebelumnya    : Rp.%,d\n", harga);
                                                System.out.print  ("Masukkan Harga Baru : ");
                                                int hargabaru = in.nextInt();
                                                writeKeTemp.write(kategori+"_"+nama+"_"+hargabaru+"_"+warna+"_"+ukuran);
                                                writeKeTemp.newLine();
                                                writeKeTemp.flush();
                                                clrscr();
                                                System.out.println("Edit Harga Jualan No." + editNum + " Kategori " + kategori + " Berhasil");
                                                break isEDIT;
                                            } else if (noEdit == 3) {
                                                ArrayList<String> warnabaru = new ArrayList<>();
                                                System.out.println("Warna Sebelumnya : " + warna);
                                                warnabaru = getwarna();
                                                writeKeTemp.write(kategori+"_"+nama+"_"+harga+"_");

                                                for (int i = 0; i < warnabaru.size(); i++) {
                                                    writeKeTemp.write(warnabaru.get(i));
                                                    writeKeTemp.flush();
                                                    if (i != warnabaru.size()-1) {
                                                        writeKeTemp.write(",");
                                                        writeKeTemp.flush();
                                                    }
                                                }

                                                writeKeTemp.write("_"+ukuran);
                                                writeKeTemp.newLine();
                                                writeKeTemp.flush();
                                                clrscr();
                                                System.out.println("Edit Warna Jualan No." + editNum + " Kategori " + kategori + " Berhasil");
                                                break isEDIT;
                                            } else if (noEdit == 4) {

                                                ArrayList<String> ukuranbaru = new ArrayList<>();
                                                System.out.println("Ukuran Sebelumnya : " + ukuran);
                                                ukuranbaru = getUkuran();
                                                writeKeTemp.write(kategori+"_"+nama+"_"+harga+"_"+warna+"_");
                                                for (int i = 0; i < ukuranbaru.size(); i++) {
                                                    writeKeTemp.write(ukuranbaru.get(i));
                                                    writeKeTemp.flush();
                                                    if (i != ukuranbaru.size()-1) {
                                                        writeKeTemp.write(",");
                                                        writeKeTemp.flush();
                                                    }
                                                }

                                                writeKeTemp.newLine();
                                                writeKeTemp.flush();
                                                clrscr();
                                                System.out.println("Edit ukuran Jualan No." + editNum + " Kategori " + kategori + " Berhasil");
                                                break isEDIT;

                                            } else if (noEdit == 0) {
                                                writeKeTemp.write(data);
                                                writeKeTemp.newLine();
                                                writeKeTemp.flush();
                                                clrscr();
                                                System.out.println("Barang Batal Diedit !");
                                                break isEDIT;
                                            } else {
                                                clrscr();
                                                System.out.println("Warning : Pilihan Tidak Tersedia !");
                                            }

                                        } catch (InputMismatchException e) {
                                            clrscr();
                                            System.out.println("Warning : Masukkan Inputan Yang Valid !");
                                        }

                                    } while (true);


                                } else if (aksi.equalsIgnoreCase("n")) {
                                    writeKeTemp.write(data);
                                    writeKeTemp.newLine();
                                    writeKeTemp.flush();
                                    clrscr();
                                    System.out.println("Barang Batal Diedit");
                                    break;
                                } else {
                                    System.out.println("Warning : Inputan Tidak Valid !");
                                }

                            } while (true);



                        } else {
                            writeKeTemp.write(data);
                            writeKeTemp.newLine();
                            writeKeTemp.flush();
                        }

                        data = readfile.readLine();
                    }

                    filer.close();
                    readfile.close();
                    filew.close();
                    writeKeTemp.close();

                    if (file.delete()) {
                        if (tempFile.renameTo(file)) {
                            start();
                        } else {
                            System.out.println("error rename");
                        }
                    } else {
                        System.out.println("Error delete");
                    }

                }

            }

        }


    }

    private static int getNum(String message, int limit, boolean getnol) {
        int nomor = 0;
        Scanner in = new Scanner(System.in);

        try {
            System.out.print(message);
            nomor =  in.nextInt();

            if (getnol) {
                if (nomor < 0 || nomor > limit) {
                    System.out.println("Warning : Jualan Tidak Ditemukan !");
                    nomor = getNum(message,limit, getnol);
                }

            } else {
                if (nomor <= 0 || nomor > limit) {
                    System.out.println("Warning : Jualan Tidak Ditemukan !");
                    nomor = getNum(message,limit, getnol);
                }
            }

        }catch (InputMismatchException e) {
            System.out.println("Warning : Inputan Tidak Valid !");
            nomor = getNum(message, limit, getnol);
        }

        return nomor;
    }

    //DELETE
    public static void deleteJualan() throws IOException {
        Scanner in = new Scanner(System.in);

        String[] jenisjualan = { "kosong",
                "Jaket",
                "Kemeja",
                "Pakaian Pria",
                "Pakaian Wanita",
                "Sepatu",
                "Tas Wanita",
                "Tas Pria",
                "Jaz",
                "Jam Tangan"
        };

        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println("Kategori Yang Tersedia : ");
        for (int i = 0; i < jenisjualan.length; i++) {
            if (i == 0) {
                continue;
            }

            if (i % 2 != 0) {
                System.out.printf("\t%d. %-20s", i, jenisjualan[i]);
            }

            if (i % 2 == 0) {
                System.out.printf("%d. %s\n", i, jenisjualan[i]);
            }
        }
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("Opsi Lain : ");
        System.out.println("\tq. Back");
        System.out.println("\te. Exit");
        System.out.println("------------------------------------------------------");
        System.out.print  ("Masukkan Kategori Jualan Yang Ingin Dihapus : ");
        String pilihan = in.next();
        if (pilihan.equalsIgnoreCase("q")) {
            clrscr();
            start();
            return;
        } else if (pilihan.equalsIgnoreCase("e")) {
            exit();
            return;
        } else {

            int index = Integer.parseInt(pilihan);

            if (index == 0 || index > jenisjualan.length-1) {
                clrscr();
                System.out.println("Warning : Pilihan Tidak Tersedia !");
                updateJualan();
            } else {
                String kategori = jenisjualan[index];
                ReadJualan checkFileJikaAda = new ReadJualan(kategori, false, "UPDATE");
                checkFileJikaAda.tampilkan();

                if (checkFileJikaAda.FilejualanTerisi) {
                    String kategori_TanpaSpasi = kategori.replaceAll(" ", "");
                    String fileJualan = kategori_TanpaSpasi + ".txt";
                    int limit = checkFileJikaAda.jumlahjualan;
                    System.out.println("======================================================");
                    System.out.println("\t0. Delete All");
                    System.out.println("------------------------------------------------------");
                    int deleteNum = getNum("Masukkan No.Barang Yang Ingin Dihapus : ",limit, true);

                    File file = new File(fileJualan);

                    if (deleteNum == 0) {
                        if (file.delete()) {
                            clrscr();
                            System.out.println("Semua Barang Dalam Kategori " + kategori + " Berhasil Dihapus !");
                            deleteJualan();
                            return;
                        }
                    }

                    File tempFile = new File("TempFile.txt");

                    FileReader filer = new FileReader(file);
                    BufferedReader readfile = new BufferedReader(filer);

                    FileWriter filew = new FileWriter(tempFile);
                    BufferedWriter writeKeTemp = new BufferedWriter(filew);

                    String data = readfile.readLine();
                    Scanner datascan;

                    int entryPoint = 0;

                    while (data != null) {
                        entryPoint++;

                        if (entryPoint == deleteNum) {
                            datascan = new Scanner(data);
                            datascan.useDelimiter("_");
                            String Kategori = datascan.next();
                            String nama = datascan.next();
                            int harga = datascan.nextInt();
                            String warna = datascan.next();
                            String ukuran = datascan.next();

                            clrscr();
                            do {
                                System.out.println("======================================================");
                                System.out.println("=================== AhmadFMA.Tech ====================");
                                System.out.println("======================================================");
                                System.out.println("Jualan Yang Ingin Dihapus : ");
                                System.out.println("Nomor : " + deleteNum);
                                System.out.println("\tKategori     : " + Kategori);
                                System.out.println("\tNama Barang  : " + nama);
                                System.out.printf ("\tHarga Barang : Rp.%,d\n", harga);
                                System.out.println("\tWarna        : " + warna);
                                System.out.println("\tUkuran       : " + ukuran);
                                System.out.println("------------------------------------------------------");
                                System.out.print  ("Yakin Ingin Menghapus ? (y/n) : ");

                                String aksi = in.next();
                                if (aksi.equalsIgnoreCase("y")) {
                                    //data diskip
                                    clrscr();
                                    System.out.println("Barang No." + deleteNum + " Kategori " + kategori + " Berhasil Dihapus");
                                    break;
                                } else if (aksi.equalsIgnoreCase("n")){
                                    writeKeTemp.write(data);
                                    writeKeTemp.newLine();
                                    writeKeTemp.flush();
                                    break;
                                } else {
                                    System.out.println("Warning : Inputan Tidak Valid !");
                                }
                            } while (true);


                        } else {
                            writeKeTemp.write(data);
                            writeKeTemp.newLine();
                            writeKeTemp.flush();
                        }

                        data = readfile.readLine();
                    }

                    filer.close();
                    readfile.close();
                    filew.close();
                    writeKeTemp.close();
                    if (file.delete()) {
                        if (tempFile.renameTo(file)) {
                            start();
                        } else {
                            clrscr();
                            System.out.println("Error Rename");
                        }
                    } else {
                        clrscr();
                        System.out.println("Error Delete");
                    }

                }

            }


        }

    }

    //SEARCH
    public static void searchJualan() throws IOException {
        Scanner in = new Scanner(System.in);

        clrscr();
        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println();
        System.out.print  ("Masukkan Keyword Barang Yang Dicari : ");
        String input = in.nextLine();
        String[] keywords = input.split("\\s");
        boolean isFound = false;
        int nomor = 0;


        String[] jenisjualan = { "kosong",
                "Jaket",
                "Kemeja",
                "Pakaian Pria",
                "Pakaian Wanita",
                "Sepatu",
                "Tas Wanita",
                "Tas Pria",
                "Jaz",
                "Jam Tangan"
        };

        clrscr();
        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        for (int i = 0; i < jenisjualan.length; i++) {
            String kategori = jenisjualan[i];
            String kategori_TanpaSpasi = kategori.replaceAll(" ", "");
            String filename = kategori_TanpaSpasi + ".txt";

            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader filer = new FileReader(file);
            BufferedReader readfile = new BufferedReader(filer);
            String data = readfile.readLine();

            if (data == null) {
                continue;
            }

            while (data != null) {

                for (String keyword : keywords) {

                    if (data.toLowerCase().contains(keyword.toLowerCase())) {
                        if (nomor == 0) {
                            System.out.println("Barang Yang Mungkin Anda Maksud : ");
                            System.out.println();
                        }
                        nomor++;
                        Scanner datascan = new Scanner(data);
                        datascan.useDelimiter("_");
                        String kategor = datascan.next();
                        String nama = datascan.next();
                        int harga = datascan.nextInt();
                        String warna = datascan.next();
                        String ukuran = datascan.next();

                        System.out.println("" + nomor + ".)");
                        System.out.println("\tKategori     : " +  kategor);
                        System.out.println("\tNama Barang  : " + nama);
                        System.out.printf ("\tHarga Barang : Rp.%,d\n", harga);
                        System.out.println("\tWarna        : " + warna);
                        System.out.println("\tUkuran       : " + ukuran);
                        System.out.println();
                        isFound = true;
                        break;
                    }

                }

                data = readfile.readLine();
            }

            filer.close();
            readfile.close();
        }

        if (!isFound) {
            System.out.println("\nBarang Tidak DiTemukan !");
            System.out.println();
        }

        System.out.println("------------------------------------------------------");
        System.out.println("Opsi Pilihan :");
        System.out.println("\tq. Back");
        System.out.println("\te. Exit");
        System.out.println("------------------------------------------------------");
        do {
            System.out.print  ("Pilih Opsi : ");
            String opsi = in.next();
            if (opsi.equalsIgnoreCase("q")) {
                clrscr();
                start();
                break;
            } else if (opsi.equalsIgnoreCase("e")) {
                clrscr();
                exit();
                break;
            } else {
                System.out.println("Warning : Pilihan Tidak Tersedia !");
            }

        } while (true);

    }

    //EXIT
    public static void exit() {
        clrscr();
        System.out.println("======================================================");
        System.out.println("=================== AhmadFMA.Tech ====================");
        System.out.println("======================================================");
        System.out.println();
        System.out.println();
        System.out.println("                 See You Next Time :)                 ");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("    Diakses pada Tanggal : " + tanggal + ", Jam : " + jam);
        System.out.println();
        System.out.println("================= Made by : AhmadFMA =================");
    }


    //utility
    public static void clrscr() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }

}
