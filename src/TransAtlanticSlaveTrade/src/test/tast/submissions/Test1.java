package test.tast.submissions;

import java.sql.*;
import java.text.*;
import java.io.*;

import edu.emory.library.tast.dm.Voyage;

public class Test1
{
	
	String[] attrib={
			"adlt1imp",
			"adlt2imp",
			"adlt3imp",
			"adult7",
			"boy7",
			"boyrat1",
			"boyrat3",
			"boyrat7",
			"chil1imp",
			"chil2imp",
			"chil3imp",
			"child7",
			"chilrat1",
			"chilrat3",
			"chilrat7",
			"constreg",
			"datebuy",
			"datedep",
			"datedepam",
			"dateend",
			"dateland1",
			"dateland2",
			"dateland3",
			"dateleftafr",
			"deptreg",
			"deptreg1",
			"deptregimp",
			"deptregimp1",
			"embreg",
			"embreg2",
			"fate2",
			"fate3",
			"fate4",
			"female7",
			"feml1imp",
			"feml2imp",
			"feml3imp",
			"girl7",
			"girlrat1",
			"girlrat3",
			"girlrat7",
			"majbuypt",
			"majbuyreg",
			"majbyimp",
			"majbyimp1",
			"majselpt",
			"majselrg",
			"male1imp",
			"male2imp",
			"male3imp",
			"male7",
			"malrat1",
			"malrat3",
			"malrat7",
			"men7",
			"menrat1",
			"menrat3",
			"menrat7",
			"mjbyptimp",
			"mjselimp",
			"mjselimp1",
			"mjslptimp",
			"natinimp",
			"ptdepimp",
			"regarr",
			"regarr2",
			"regdis1",
			"regdis2",
			"regdis3",
			"regem1",
			"regem2",
			"regem3",
			"regisreg",
			"retrnreg",
			"retrnreg1",
			"slamimp",
			"slavema1",
			"slavema3",
			"slavema7",
			"slavemx1",
			"slavemx3",
			"slavemx7",
			"slavmax1",
			"slavmax3",
			"slavmax7",
			"slaximp",
			"tonmod",
			"tslmtimp",
			"voy1imp",
			"voy2imp",
			"vymrtimp",
			"vymrtrat",
			"women7",
			"womrat1",
			"womrat3",
			"womrat7",
			"xmimpflag",
			"year10",
			"year100",
			"year25",
			"year5",
			"yearaf",
			"yearam",
			"yeardep"
			};

	
	
  Connection       db;        
  Statement        sql;       
  DatabaseMetaData dbmd;      
                              

  public String[][] answer(int VoyId) throws ClassNotFoundException, SQLException
  {
	String[][] ret = new String[attrib.length][3]; //Array to return
	String query="SELECT t.adlt1imp AS adlt1imp_t, t.adlt2imp AS adlt2imp_t, t.adlt3imp AS adlt3imp_t, t.adult7 AS adult7_t, t.boy7 AS boy7_t, t.boyrat1 AS boyrat1_t, t.boyrat3 AS boyrat3_t, t.boyrat7 AS boyrat7_t, t.chil1imp AS chil1imp_t, t.chil2imp AS chil2imp_t, t.chil3imp AS chil3imp_t, t.child7 AS child7_t, t.chilrat1 AS chilrat1_t, t.chilrat3 AS chilrat3_t, t.chilrat7 AS chilrat7_t, t.constreg AS constreg_t, t.date_buy AS datebuy_t, t.date_dep AS datedep_t, t.date_depam AS datedepam_t, t.date_end AS dateend_t, t.date_land1 AS dateland1_t, t.date_land2 AS dateland2_t, t.date_land3 AS dateland3_t, t.date_leftafr AS dateleftafr_t, t.deptreg AS deptreg_t, t.deptreg1 AS deptreg1_t, t.deptregimp AS deptregimp_t, t.deptregimp1 AS deptregimp1_t, t.embreg AS embreg_t, t.embreg2 AS embreg2_t, t.fate2 AS fate2_t, t.fate3 AS fate3_t, t.fate4 AS fate4_t, t.female7 AS female7_t, t.feml1imp AS feml1imp_t, t.feml2imp AS feml2imp_t, t.feml3imp AS feml3imp_t, t.girl7 AS girl7_t, t.girlrat1 AS girlrat1_t, t.girlrat3 AS girlrat3_t, t.girlrat7 AS girlrat7_t, t.majbuypt AS majbuypt_t, t.majbuyreg AS majbuyreg_t, t.majbyimp AS majbyimp_t, t.majbyimp1 AS majbyimp1_t, t.majselpt AS majselpt_t, t.majselrg AS majselrg_t, t.male1imp AS male1imp_t, t.male2imp AS male2imp_t, t.male3imp AS male3imp_t, t.male7 AS male7_t, t.malrat1 AS malrat1_t, t.malrat3 AS malrat3_t, t.malrat7 AS malrat7_t, t.men7 AS men7_t, t.menrat1 AS menrat1_t, t.menrat3 AS menrat3_t, t.menrat7 AS menrat7_t, t.mjbyptimp AS mjbyptimp_t, t.mjselimp AS mjselimp_t, t.mjselimp1 AS mjselimp1_t, t.mjslptimp AS mjslptimp_t, t.natinimp AS natinimp_t, t.ptdepimp AS ptdepimp_t, t.regarr AS regarr_t, t.regarr2 AS regarr2_t, t.regdis1 AS regdis1_t, t.regdis2 AS regdis2_t, t.regdis3 AS regdis3_t, t.regem1 AS regem1_t, t.regem2 AS regem2_t, t.regem3 AS regem3_t, t.regisreg AS regisreg_t, t.retrnreg AS retrnreg_t, t.retrnreg1 AS retrnreg1_t, t.slamimp AS slamimp_t, t.slavema1 AS slavema1_t, t.slavema3 AS slavema3_t, t.slavema7 AS slavema7_t, t.slavemx1 AS slavemx1_t, t.slavemx3 AS slavemx3_t, t.slavemx7 AS slavemx7_t, t.slavmax1 AS slavmax1_t, t.slavmax3 AS slavmax3_t, t.slavmax7 AS slavmax7_t, t.slaximp AS slaximp_t, t.tonmod AS tonmod_t, t.tslmtimp AS tslmtimp_t, t.voy1imp AS voy1imp_t, t.voy2imp AS voy2imp_t, t.vymrtimp AS vymrtimp_t, t.vymrtrat AS vymrtrat_t, t.women7 AS women7_t, t.womrat1 AS womrat1_t, t.womrat3 AS womrat3_t, t.womrat7 AS womrat7_t, t.xmimpflag AS xmimpflag_t, t.year10 AS year10_t, t.year100 AS year100_t, t.year25 AS year25_t, t.year5 AS year5_t, t.yearaf AS yearaf_t, t.yearam AS yearam_t, t.yeardep AS yeardep_t, v.adlt1imp AS adlt1imp_v, v.adlt2imp AS adlt2imp_v, v.adlt3imp AS adlt3imp_v, v.adult7 AS adult7_v, v.boy7 AS boy7_v, v.boyrat1 AS boyrat1_v, v.boyrat3 AS boyrat3_v, v.boyrat7 AS boyrat7_v, v.chil1imp AS chil1imp_v, v.chil2imp AS chil2imp_v, v.chil3imp AS chil3imp_v, v.child7 AS child7_v, v.chilrat1 AS chilrat1_v, v.chilrat3 AS chilrat3_v, v.chilrat7 AS chilrat7_v, v.constreg AS constreg_v, v.datebuy AS datebuy_v, v.datedep AS datedep_v, v.datedepam AS datedepam_v, v.dateend AS dateend_v, v.dateland1 AS dateland1_v, v.dateland2 AS dateland2_v, v.dateland3 AS dateland3_v, v.dateleftafr AS dateleftafr_v, v.deptreg AS deptreg_v, v.deptreg1 AS deptreg1_v, v.deptregimp AS deptregimp_v, v.deptregimp1 AS deptregimp1_v, v.embreg AS embreg_v, v.embreg2 AS embreg2_v, v.fate2 AS fate2_v, v.fate3 AS fate3_v, v.fate4 AS fate4_v, v.female7 AS female7_v, v.feml1imp AS feml1imp_v, v.feml2imp AS feml2imp_v, v.feml3imp AS feml3imp_v, v.girl7 AS girl7_v, v.girlrat1 AS girlrat1_v, v.girlrat3 AS girlrat3_v, v.girlrat7 AS girlrat7_v, v.majbuypt AS majbuypt_v, v.majbuyreg AS majbuyreg_v, v.majbyimp AS majbyimp_v, v.majbyimp1 AS majbyimp1_v, v.majselpt AS majselpt_v, v.majselrg AS majselrg_v, v.male1imp AS male1imp_v, v.male2imp AS male2imp_v, v.male3imp AS male3imp_v, v.male7 AS male7_v, v.malrat1 AS malrat1_v, v.malrat3 AS malrat3_v, v.malrat7 AS malrat7_v, v.men7 AS men7_v, v.menrat1 AS menrat1_v, v.menrat3 AS menrat3_v, v.menrat7 AS menrat7_v, v.mjbyptimp AS mjbyptimp_v, v.mjselimp AS mjselimp_v, v.mjselimp1 AS mjselimp1_v, v.mjslptimp AS mjslptimp_v, v.natinimp AS natinimp_v, v.ptdepimp AS ptdepimp_v, v.regarr AS regarr_v, v.regarr2 AS regarr2_v, v.regdis1 AS regdis1_v, v.regdis2 AS regdis2_v, v.regdis3 AS regdis3_v, v.regem1 AS regem1_v, v.regem2 AS regem2_v, v.regem3 AS regem3_v, v.regisreg AS regisreg_v, v.retrnreg AS retrnreg_v, v.retrnreg1 AS retrnreg1_v, v.slamimp AS slamimp_v, v.slavema1 AS slavema1_v, v.slavema3 AS slavema3_v, v.slavema7 AS slavema7_v, v.slavemx1 AS slavemx1_v, v.slavemx3 AS slavemx3_v, v.slavemx7 AS slavemx7_v, v.slavmax1 AS slavmax1_v, v.slavmax3 AS slavmax3_v, v.slavmax7 AS slavmax7_v, v.slaximp AS slaximp_v, v.tonmod AS tonmod_v, v.tslmtimp AS tslmtimp_v, v.voy1imp AS voy1imp_v, v.voy2imp AS voy2imp_v, v.vymrtimp AS vymrtimp_v, v.vymrtrat AS vymrtrat_v, v.women7 AS women7_v, v.womrat1 AS womrat1_v, v.womrat3 AS womrat3_v, v.womrat7 AS womrat7_v, v.xmimpflag AS xmimpflag_v, v.year10 AS year10_v, v.year100 AS year100_v, v.year25 AS year25_v, v.year5 AS year5_v, v.yearaf AS yearaf_v, v.yearam AS yearam_v, v.yeardep AS yeardep_v FROM test1 AS t, voyages AS v WHERE v.voyageid=t.voyageid AND v.revision=1 AND v.voyageid='"+VoyId+"'";
	//System.out.println(query);
	
    String database = "tast";
    String username = "tast";
    String password = "pass_1234";
    Class.forName("org.postgresql.Driver"); //load the driver
    db = DriverManager.getConnection("jdbc:postgresql:"+database,
                                     username,
                                     password);
    dbmd = db.getMetaData();
    //System.out.println("Connected...");
    sql = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
 
    ResultSet results = sql.executeQuery(query);
    
    
    if (results != null)
    {
      results.first();
      
      for(int i=0; i< attrib.length; i++)
      {
    	  String tField=results.getString(attrib[i]+"_t");
    	  String vField=results.getString(attrib[i]+"_v");
    	  ret[i][0]=attrib[i];
    	  ret[i][1]=vField;
    	  ret[i][2]=tField;
    	  //System.out.println(attrib[i]+": " + vField + " " + tField);
      }
    }
    
    results.close();
    db.close();
    
  return ret;
  }

}