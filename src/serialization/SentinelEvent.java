package serialization;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.hadoop.classification.InterfaceStability.Unstable;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;


public class SentinelEvent implements Serializable {
	
	private static final long serialVersionUID = 3763981542818718386L;

	public Map<String,String> valueMap = new HashMap<String,String>();

	public static final String[] fieldIDCache = {
		"iuid",
		"iufname",
		"iudep",
		"iuident",
		"iwfid",
		"euname",
		"euid",
		"eudom",
		"iemail",
		"iup",
		"sun",
		"rv35",
		"shn",
		"smac",
		"sxip",
		"sxmac",
		"sxport",
		"rv42",
		"sip",
		"sp",
		"isvcc",
		"spint",
		"srclat",
		"srclong",
		"rv59",
		"rv60",
		"rv62",
		"rv76",
		"rv77",
		"rv29",
		"sev",
		"msg",
		"prot",
		"rv40",
		"voc",
		"polid",
		"rv145",
		"rc",
		"evtgrpid",
		"sessid",
		"ei",
		"dt",
		"det",
		"spt",
		"bgnt",
		"endt",
		"estz",
		"estzmonth",
		"estzdiy",
		"estzdim",
		"estzdiw",
		"estzhour",
		"estzmin",
		"tuid",
		"tufname",
		"tudep",
		"tuident",
		"twfid",
		"dlat",
		"dlong",
		"dnewcont",
		"dnewspace",
		"ttn",
		"ttid",
		"ttd",
		"temail",
		"dun",
		"rv45",
		"dhn",
		"rv41",
		"dip",
		"dp",
		"dnewname",
		"dnewtype",
		"attr",
		"rv43",
		"dmac",
		"dxip",
		"dxport",
		"dxmac",
		"tsvcc",
		"tdspace",
		"rv36",
		"tds",
		"dpint",
		"rv141",
		"rv81",
		"rv82",
		"rv84",
		"rv98",
		"rv99",
		"rv30",
		"obsip",
		"obsdom",
		"obsmac",
		"obscountry",
		"obslat",
		"obslong",
		"obssvcname",
		"obsassetid",
		"obscrit",
		"obsclass",
		"obsfunc",
		"obsdep",
		"sn",
		"rv32",
		"rv39",
		"rv49",
		"rv54",
		"rv55",
		"rv100",
		"rv150",
		"repip",
		"repdom",
		"repmac",
		"repassetid",
		"repcrit",
		"repclass",
		"repfunc",
		"repdep",
		"rv31",
		"xdasclass",
		"xdasdetail",
		"xdasid",
		"xdasoutcome",
		"xdasoutcomename",
		"xdasprov",
		"xdasreg",
		"xdastaxname",
		"rv21",
		"src",
		"rv122",
		"rv123",
		"rv124",
		"rv22",
		"rv23",
		"rv24",
		"rv25",
		"rv28",
		"id",
		"rv121",
		"rv171",
		"rv172",
		"rv164",
		"rv165",
		"rv101",
		"res",
		"sres",
		"rv191",
		"rv192",
		"rv193",
		"rv50",
		"rv51",
		"rv52",
		"rv53",
		"rv27",
		"rv26",
		"rv33",
		"cv93",
		"cv92",
		"cv91",
		"cv94",
		"cv90",
		"rv46",
		"rv37",
		"rv38",
		"rv34",
		"rv47",
		"rv44",
		"rv48",
		"rv142",
		"et",
		"rv2",
		"rv1",
		"ceu",
		"ct3",
		"rt3",
		"ct2",
		"ct1",
		"rt2",
		"rt1",
		"pn",
		"rn",
		"fn",
		"st",
		"evt",
		"agent",
		"port",
		"crt",
		"vul",
		"rv3",
		"rv4",
		"rv5",
		"rv6",
		"rv7",
		"rv8",
		"rv9",
		"rv10",
		"rv11",
		"rv12",
		"rv13",
		"rv14",
		"rv15",
		"rv16",
		"rv17",
		"rv18",
		"rv19",
		"rv20",
		"rv56",
		"rv57",
		"rv58",
		"rv61",
		"rv63",
		"rv64",
		"rv65",
		"rv66",
		"rv67",
		"rv68",
		"rv69",
		"rv70",
		"rv71",
		"rv72",
		"rv73",
		"rv74",
		"rv75",
		"rv78",
		"rv79",
		"rv80",
		"rv83",
		"rv85",
		"rv86",
		"rv87",
		"rv88",
		"rv89",
		"rv90",
		"rv91",
		"rv92",
		"rv93",
		"rv94",
		"rv95",
		"rv96",
		"rv97",
		"rv102",
		"rv103",
		"rv104",
		"rv105",
		"rv106",
		"rv107",
		"rv108",
		"rv109",
		"rv110",
		"rv111",
		"rv112",
		"rv113",
		"rv114",
		"rv115",
		"rv116",
		"rv117",
		"rv118",
		"rv119",
		"rv120",
		"rv125",
		"rv126",
		"rv127",
		"rv128",
		"rv129",
		"rv130",
		"rv131",
		"rv132",
		"rv133",
		"rv134",
		"rv135",
		"rv136",
		"rv137",
		"rv138",
		"rv139",
		"rv140",
		"rv143",
		"rv144",
		"rv146",
		"rv147",
		"rv148",
		"rv149",
		"rv151",
		"rv152",
		"rv153",
		"rv154",
		"rv155",
		"rv156",
		"rv157",
		"rv158",
		"rv159",
		"rv160",
		"rv161",
		"rv162",
		"rv163",
		"rv166",
		"rv167",
		"rv168",
		"rv169",
		"rv170",
		"rv173",
		"rv174",
		"rv175",
		"rv176",
		"rv177",
		"rv178",
		"rv179",
		"rv180",
		"rv181",
		"rv182",
		"rv183",
		"rv184",
		"rv185",
		"rv186",
		"rv187",
		"rv188",
		"rv189",
		"rv190",
		"rv194",
		"rv195",
		"rv196",
		"rv197",
		"rv198",
		"rv199",
		"rv200",
		"cv1",
		"cv2",
		"cv3",
		"cv4",
		"cv5",
		"cv6",
		"cv7",
		"cv8",
		"cv9",
		"cv10",
		"cv11",
		"cv12",
		"cv13",
		"cv14",
		"cv15",
		"cv16",
		"cv17",
		"cv18",
		"cv19",
		"cv20",
		"cv21",
		"cv22",
		"cv23",
		"cv24",
		"cv25",
		"cv26",
		"cv27",
		"cv28",
		"cv29",
		"cv30",
		"cv31",
		"cv32",
		"cv33",
		"cv34",
		"cv35",
		"cv36",
		"cv37",
		"cv38",
		"cv39",
		"cv40",
		"cv41",
		"cv42",
		"cv43",
		"cv44",
		"cv45",
		"cv46",
		"cv47",
		"cv48",
		"cv49",
		"cv50",
		"cv51",
		"cv52",
		"cv53",
		"cv54",
		"cv55",
		"cv56",
		"cv57",
		"cv58",
		"cv59",
		"cv60",
		"cv61",
		"cv62",
		"cv63",
		"cv64",
		"cv65",
		"cv66",
		"cv67",
		"cv68",
		"cv69",
		"cv70",
		"cv71",
		"cv72",
		"cv73",
		"cv74",
		"cv75",
		"cv76",
		"cv77",
		"cv78",
		"cv79",
		"cv80",
		"cv81",
		"cv82",
		"cv83",
		"cv84",
		"cv85",
		"cv86",
		"cv87",
		"cv88",
		"cv89",
		"cv95",
		"cv96",
		"cv97",
		"cv98",
		"cv99",
		"cv100",
		"cv101",
		"cv102",
		"cv103",
		"cv104",
		"cv105",
		"cv106",
		"cv107",
		"cv108",
		"cv109",
		"cv110",
		"cv111",
		"cv112",
		"cv113",
		"cv114",
		"cv115",
		"cv116",
		"cv117",
		"cv118",
		"cv119",
		"cv120",
		"cv121",
		"cv122",
		"cv123",
		"cv124",
		"cv125",
		"cv126",
		"cv127",
		"cv128",
		"cv129",
		"cv130",
		"cv131",
		"cv132",
		"cv133",
		"cv134",
		"cv135",
		"cv136",
		"cv137",
		"cv138",
		"cv139",
		"cv140",
		"cv141",
		"cv142",
		"cv143",
		"cv144",
		"cv145",
		"cv146",
		"cv147",
		"cv148",
		"cv149",
		"cv150",
		"cv151",
		"cv152",
		"cv153",
		"cv154",
		"cv155",
		"cv156",
		"cv157",
		"cv158",
		"cv159",
		"cv160",
		"cv161",
		"cv162",
		"cv163",
		"cv164",
		"cv165",
		"cv166",
		"cv167",
		"cv168",
		"cv169",
		"cv170",
		"cv171",
		"cv172",
		"cv173",
		"cv174",
		"cv175",
		"cv176",
		"cv177",
		"cv178",
		"cv179",
		"cv180",
		"cv181",
		"cv182",
		"cv183",
		"cv184",
		"cv185",
		"cv186",
		"cv187",
		"cv188",
		"cv189",
		"cv190",
		"cv191",
		"cv192",
		"cv193",
		"cv194",
		"cv195",
		"cv196",
		"cv197",
		"cv198",
		"cv199",
		"cv200"
	};

	
	public static final class Field {
		public static final int iuid = 0;
		public static final int iufname = 1;
		public static final int iudep = 2;
		public static final int iuident = 3;
		public static final int iwfid = 4;
		public static final int euname = 5;
		public static final int euid = 6;
		public static final int eudom = 7;
		public static final int iemail = 8;
		public static final int iup = 9;
		public static final int sun = 10;
		public static final int rv35 = 11;
		public static final int shn = 12;
		public static final int smac = 13;
		public static final int sxip = 14;
		public static final int sxmac = 15;
		public static final int sxport = 16;
		public static final int rv42 = 17;
		public static final int sip = 18;
		public static final int sp = 19;
		public static final int isvcc = 20;
		public static final int spint = 21;
		public static final int srclat = 22;
		public static final int srclong = 23;
		public static final int rv59 = 24;
		public static final int rv60 = 25;
		public static final int rv62 = 26;
		public static final int rv76 = 27;
		public static final int rv77 = 28;
		public static final int rv29 = 29;
		public static final int sev = 30;
		public static final int msg = 31;
		public static final int prot = 32;
		public static final int rv40 = 33;
		public static final int voc = 34;
		public static final int polid = 35;
		public static final int rv145 = 36;
		public static final int rc = 37;
		public static final int evtgrpid = 38;
		public static final int sessid = 39;
		public static final int ei = 40;
		public static final int dt = 41;
		public static final int det = 42;
		public static final int spt = 43;
		public static final int bgnt = 44;
		public static final int endt = 45;
		public static final int estz = 46;
		public static final int estzmonth = 47;
		public static final int estzdiy = 48;
		public static final int estzdim = 49;
		public static final int estzdiw = 50;
		public static final int estzhour = 51;
		public static final int estzmin = 52;
		public static final int tuid = 53;
		public static final int tufname = 54;
		public static final int tudep = 55;
		public static final int tuident = 56;
		public static final int twfid = 57;
		public static final int dlat = 58;
		public static final int dlong = 59;
		public static final int dnewcont = 60;
		public static final int dnewspace = 61;
		public static final int ttn = 62;
		public static final int ttid = 63;
		public static final int ttd = 64;
		public static final int temail = 65;
		public static final int dun = 66;
		public static final int rv45 = 67;
		public static final int dhn = 68;
		public static final int rv41 = 69;
		public static final int dip = 70;
		public static final int dp = 71;
		public static final int dnewname = 72;
		public static final int dnewtype = 73;
		public static final int attr = 74;
		public static final int rv43 = 75;
		public static final int dmac = 76;
		public static final int dxip = 77;
		public static final int dxport = 78;
		public static final int dxmac = 79;
		public static final int tsvcc = 80;
		public static final int tdspace = 81;
		public static final int rv36 = 82;
		public static final int tds = 83;
		public static final int dpint = 84;
		public static final int rv141 = 85;
		public static final int rv81 = 86;
		public static final int rv82 = 87;
		public static final int rv84 = 88;
		public static final int rv98 = 89;
		public static final int rv99 = 90;
		public static final int rv30 = 91;
		public static final int obsip = 92;
		public static final int obsdom = 93;
		public static final int obsmac = 94;
		public static final int obscountry = 95;
		public static final int obslat = 96;
		public static final int obslong = 97;
		public static final int obssvcname = 98;
		public static final int obsassetid = 99;
		public static final int obscrit = 100;
		public static final int obsclass = 101;
		public static final int obsfunc = 102;
		public static final int obsdep = 103;
		public static final int sn = 104;
		public static final int rv32 = 105;
		public static final int rv39 = 106;
		public static final int rv49 = 107;
		public static final int rv54 = 108;
		public static final int rv55 = 109;
		public static final int rv100 = 110;
		public static final int rv150 = 111;
		public static final int repip = 112;
		public static final int repdom = 113;
		public static final int repmac = 114;
		public static final int repassetid = 115;
		public static final int repcrit = 116;
		public static final int repclass = 117;
		public static final int repfunc = 118;
		public static final int repdep = 119;
		public static final int rv31 = 120;
		public static final int xdasclass = 121;
		public static final int xdasdetail = 122;
		public static final int xdasid = 123;
		public static final int xdasoutcome = 124;
		public static final int xdasoutcomename = 125;
		public static final int xdasprov = 126;
		public static final int xdasreg = 127;
		public static final int xdastaxname = 128;
		public static final int rv21 = 129;
		public static final int src = 130;
		public static final int rv122 = 131;
		public static final int rv123 = 132;
		public static final int rv124 = 133;
		public static final int rv22 = 134;
		public static final int rv23 = 135;
		public static final int rv24 = 136;
		public static final int rv25 = 137;
		public static final int rv28 = 138;
		public static final int id = 139;
		public static final int rv121 = 140;
		public static final int rv171 = 141;
		public static final int rv172 = 142;
		public static final int rv164 = 143;
		public static final int rv165 = 144;
		public static final int rv101 = 145;
		public static final int res = 146;
		public static final int sres = 147;
		public static final int rv191 = 148;
		public static final int rv192 = 149;
		public static final int rv193 = 150;
		public static final int rv50 = 151;
		public static final int rv51 = 152;
		public static final int rv52 = 153;
		public static final int rv53 = 154;
		public static final int rv27 = 155;
		public static final int rv26 = 156;
		public static final int rv33 = 157;
		public static final int cv93 = 158;
		public static final int cv92 = 159;
		public static final int cv91 = 160;
		public static final int cv94 = 161;
		public static final int cv90 = 162;
		public static final int rv46 = 163;
		public static final int rv37 = 164;
		public static final int rv38 = 165;
		public static final int rv34 = 166;
		public static final int rv47 = 167;
		public static final int rv44 = 168;
		public static final int rv48 = 169;
		public static final int rv142 = 170;
		public static final int et = 171;
		public static final int rv2 = 172;
		public static final int rv1 = 173;
		public static final int ceu = 174;
		public static final int ct3 = 175;
		public static final int rt3 = 176;
		public static final int ct2 = 177;
		public static final int ct1 = 178;
		public static final int rt2 = 179;
		public static final int rt1 = 180;
		public static final int pn = 181;
		public static final int rn = 182;
		public static final int fn = 183;
		public static final int st = 184;
		public static final int evt = 185;
		public static final int agent = 186;
		public static final int port = 187;
		public static final int crt = 188;
		public static final int vul = 189;
		public static final int rv3 = 190;
		public static final int rv4 = 191;
		public static final int rv5 = 192;
		public static final int rv6 = 193;
		public static final int rv7 = 194;
		public static final int rv8 = 195;
		public static final int rv9 = 196;
		public static final int rv10 = 197;
		public static final int rv11 = 198;
		public static final int rv12 = 199;
		public static final int rv13 = 200;
		public static final int rv14 = 201;
		public static final int rv15 = 202;
		public static final int rv16 = 203;
		public static final int rv17 = 204;
		public static final int rv18 = 205;
		public static final int rv19 = 206;
		public static final int rv20 = 207;
		public static final int rv56 = 208;
		public static final int rv57 = 209;
		public static final int rv58 = 210;
		public static final int rv61 = 211;
		public static final int rv63 = 212;
		public static final int rv64 = 213;
		public static final int rv65 = 214;
		public static final int rv66 = 215;
		public static final int rv67 = 216;
		public static final int rv68 = 217;
		public static final int rv69 = 218;
		public static final int rv70 = 219;
		public static final int rv71 = 220;
		public static final int rv72 = 221;
		public static final int rv73 = 222;
		public static final int rv74 = 223;
		public static final int rv75 = 224;
		public static final int rv78 = 225;
		public static final int rv79 = 226;
		public static final int rv80 = 227;
		public static final int rv83 = 228;
		public static final int rv85 = 229;
		public static final int rv86 = 230;
		public static final int rv87 = 231;
		public static final int rv88 = 232;
		public static final int rv89 = 233;
		public static final int rv90 = 234;
		public static final int rv91 = 235;
		public static final int rv92 = 236;
		public static final int rv93 = 237;
		public static final int rv94 = 238;
		public static final int rv95 = 239;
		public static final int rv96 = 240;
		public static final int rv97 = 241;
		public static final int rv102 = 242;
		public static final int rv103 = 243;
		public static final int rv104 = 244;
		public static final int rv105 = 245;
		public static final int rv106 = 246;
		public static final int rv107 = 247;
		public static final int rv108 = 248;
		public static final int rv109 = 249;
		public static final int rv110 = 250;
		public static final int rv111 = 251;
		public static final int rv112 = 252;
		public static final int rv113 = 253;
		public static final int rv114 = 254;
		public static final int rv115 = 255;
		public static final int rv116 = 256;
		public static final int rv117 = 257;
		public static final int rv118 = 258;
		public static final int rv119 = 259;
		public static final int rv120 = 260;
		public static final int rv125 = 261;
		public static final int rv126 = 262;
		public static final int rv127 = 263;
		public static final int rv128 = 264;
		public static final int rv129 = 265;
		public static final int rv130 = 266;
		public static final int rv131 = 267;
		public static final int rv132 = 268;
		public static final int rv133 = 269;
		public static final int rv134 = 270;
		public static final int rv135 = 271;
		public static final int rv136 = 272;
		public static final int rv137 = 273;
		public static final int rv138 = 274;
		public static final int rv139 = 275;
		public static final int rv140 = 276;
		public static final int rv143 = 277;
		public static final int rv144 = 278;
		public static final int rv146 = 279;
		public static final int rv147 = 280;
		public static final int rv148 = 281;
		public static final int rv149 = 282;
		public static final int rv151 = 283;
		public static final int rv152 = 284;
		public static final int rv153 = 285;
		public static final int rv154 = 286;
		public static final int rv155 = 287;
		public static final int rv156 = 288;
		public static final int rv157 = 289;
		public static final int rv158 = 290;
		public static final int rv159 = 291;
		public static final int rv160 = 292;
		public static final int rv161 = 293;
		public static final int rv162 = 294;
		public static final int rv163 = 295;
		public static final int rv166 = 296;
		public static final int rv167 = 297;
		public static final int rv168 = 298;
		public static final int rv169 = 299;
		public static final int rv170 = 300;
		public static final int rv173 = 301;
		public static final int rv174 = 302;
		public static final int rv175 = 303;
		public static final int rv176 = 304;
		public static final int rv177 = 305;
		public static final int rv178 = 306;
		public static final int rv179 = 307;
		public static final int rv180 = 308;
		public static final int rv181 = 309;
		public static final int rv182 = 310;
		public static final int rv183 = 311;
		public static final int rv184 = 312;
		public static final int rv185 = 313;
		public static final int rv186 = 314;
		public static final int rv187 = 315;
		public static final int rv188 = 316;
		public static final int rv189 = 317;
		public static final int rv190 = 318;
		public static final int rv194 = 319;
		public static final int rv195 = 320;
		public static final int rv196 = 321;
		public static final int rv197 = 322;
		public static final int rv198 = 323;
		public static final int rv199 = 324;
		public static final int rv200 = 325;
		public static final int cv1 = 326;
		public static final int cv2 = 327;
		public static final int cv3 = 328;
		public static final int cv4 = 329;
		public static final int cv5 = 330;
		public static final int cv6 = 331;
		public static final int cv7 = 332;
		public static final int cv8 = 333;
		public static final int cv9 = 334;
		public static final int cv10 = 335;
		public static final int cv11 = 336;
		public static final int cv12 = 337;
		public static final int cv13 = 338;
		public static final int cv14 = 339;
		public static final int cv15 = 340;
		public static final int cv16 = 341;
		public static final int cv17 = 342;
		public static final int cv18 = 343;
		public static final int cv19 = 344;
		public static final int cv20 = 345;
		public static final int cv21 = 346;
		public static final int cv22 = 347;
		public static final int cv23 = 348;
		public static final int cv24 = 349;
		public static final int cv25 = 350;
		public static final int cv26 = 351;
		public static final int cv27 = 352;
		public static final int cv28 = 353;
		public static final int cv29 = 354;
		public static final int cv30 = 355;
		public static final int cv31 = 356;
		public static final int cv32 = 357;
		public static final int cv33 = 358;
		public static final int cv34 = 359;
		public static final int cv35 = 360;
		public static final int cv36 = 361;
		public static final int cv37 = 362;
		public static final int cv38 = 363;
		public static final int cv39 = 364;
		public static final int cv40 = 365;
		public static final int cv41 = 366;
		public static final int cv42 = 367;
		public static final int cv43 = 368;
		public static final int cv44 = 369;
		public static final int cv45 = 370;
		public static final int cv46 = 371;
		public static final int cv47 = 372;
		public static final int cv48 = 373;
		public static final int cv49 = 374;
		public static final int cv50 = 375;
		public static final int cv51 = 376;
		public static final int cv52 = 377;
		public static final int cv53 = 378;
		public static final int cv54 = 379;
		public static final int cv55 = 380;
		public static final int cv56 = 381;
		public static final int cv57 = 382;
		public static final int cv58 = 383;
		public static final int cv59 = 384;
		public static final int cv60 = 385;
		public static final int cv61 = 386;
		public static final int cv62 = 387;
		public static final int cv63 = 388;
		public static final int cv64 = 389;
		public static final int cv65 = 390;
		public static final int cv66 = 391;
		public static final int cv67 = 392;
		public static final int cv68 = 393;
		public static final int cv69 = 394;
		public static final int cv70 = 395;
		public static final int cv71 = 396;
		public static final int cv72 = 397;
		public static final int cv73 = 398;
		public static final int cv74 = 399;
		public static final int cv75 = 400;
		public static final int cv76 = 401;
		public static final int cv77 = 402;
		public static final int cv78 = 403;
		public static final int cv79 = 404;
		public static final int cv80 = 405;
		public static final int cv81 = 406;
		public static final int cv82 = 407;
		public static final int cv83 = 408;
		public static final int cv84 = 409;
		public static final int cv85 = 410;
		public static final int cv86 = 411;
		public static final int cv87 = 412;
		public static final int cv88 = 413;
		public static final int cv89 = 414;
		public static final int cv95 = 415;
		public static final int cv96 = 416;
		public static final int cv97 = 417;
		public static final int cv98 = 418;
		public static final int cv99 = 419;
		public static final int cv100 = 420;
		public static final int cv101 = 421;
		public static final int cv102 = 422;
		public static final int cv103 = 423;
		public static final int cv104 = 424;
		public static final int cv105 = 425;
		public static final int cv106 = 426;
		public static final int cv107 = 427;
		public static final int cv108 = 428;
		public static final int cv109 = 429;
		public static final int cv110 = 430;
		public static final int cv111 = 431;
		public static final int cv112 = 432;
		public static final int cv113 = 433;
		public static final int cv114 = 434;
		public static final int cv115 = 435;
		public static final int cv116 = 436;
		public static final int cv117 = 437;
		public static final int cv118 = 438;
		public static final int cv119 = 439;
		public static final int cv120 = 440;
		public static final int cv121 = 441;
		public static final int cv122 = 442;
		public static final int cv123 = 443;
		public static final int cv124 = 444;
		public static final int cv125 = 445;
		public static final int cv126 = 446;
		public static final int cv127 = 447;
		public static final int cv128 = 448;
		public static final int cv129 = 449;
		public static final int cv130 = 450;
		public static final int cv131 = 451;
		public static final int cv132 = 452;
		public static final int cv133 = 453;
		public static final int cv134 = 454;
		public static final int cv135 = 455;
		public static final int cv136 = 456;
		public static final int cv137 = 457;
		public static final int cv138 = 458;
		public static final int cv139 = 459;
		public static final int cv140 = 460;
		public static final int cv141 = 461;
		public static final int cv142 = 462;
		public static final int cv143 = 463;
		public static final int cv144 = 464;
		public static final int cv145 = 465;
		public static final int cv146 = 466;
		public static final int cv147 = 467;
		public static final int cv148 = 468;
		public static final int cv149 = 469;
		public static final int cv150 = 470;
		public static final int cv151 = 471;
		public static final int cv152 = 472;
		public static final int cv153 = 473;
		public static final int cv154 = 474;
		public static final int cv155 = 475;
		public static final int cv156 = 476;
		public static final int cv157 = 477;
		public static final int cv158 = 478;
		public static final int cv159 = 479;
		public static final int cv160 = 480;
		public static final int cv161 = 481;
		public static final int cv162 = 482;
		public static final int cv163 = 483;
		public static final int cv164 = 484;
		public static final int cv165 = 485;
		public static final int cv166 = 486;
		public static final int cv167 = 487;
		public static final int cv168 = 488;
		public static final int cv169 = 489;
		public static final int cv170 = 490;
		public static final int cv171 = 491;
		public static final int cv172 = 492;
		public static final int cv173 = 493;
		public static final int cv174 = 494;
		public static final int cv175 = 495;
		public static final int cv176 = 496;
		public static final int cv177 = 497;
		public static final int cv178 = 498;
		public static final int cv179 = 499;
		public static final int cv180 = 500;
		public static final int cv181 = 501;
		public static final int cv182 = 502;
		public static final int cv183 = 503;
		public static final int cv184 = 504;
		public static final int cv185 = 505;
		public static final int cv186 = 506;
		public static final int cv187 = 507;
		public static final int cv188 = 508;
		public static final int cv189 = 509;
		public static final int cv190 = 510;
		public static final int cv191 = 511;
		public static final int cv192 = 512;
		public static final int cv193 = 513;
		public static final int cv194 = 514;
		public static final int cv195 = 515;
		public static final int cv196 = 516;
		public static final int cv197 = 517;
		public static final int cv198 = 518;
		public static final int cv199 = 519;
		public static final int cv200 = 520;
	};

	public SentinelEvent() {
		set(Field.id, UUID.randomUUID().toString());
	}
	
	public SentinelEvent(Map<String,String> map) {
		valueMap = map;
	}
				
	public void set(int index, String value) {
		setUnsafe(fieldIDCache[index], value);
	}
	
	@Unstable
	public void setUnsafe(String key, String value) {
		if(key == null) {
			throw new IllegalArgumentException("Null keys are not supported.");
		}
		if(value != null && value.length() > 0) {
			valueMap.put(key, value);
		}
	}
	
	public String get(int index) {
		return valueMap.get(fieldIDCache[index]);
	}			
};

