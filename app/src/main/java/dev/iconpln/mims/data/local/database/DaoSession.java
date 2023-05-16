package dev.iconpln.mims.data.local.database;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import dev.iconpln.mims.data.local.database.TMonitoring;
import dev.iconpln.mims.data.local.database.TPengujian;
import dev.iconpln.mims.data.local.database.TPengujianDetails;
import dev.iconpln.mims.data.local.database.TLokasi;
import dev.iconpln.mims.data.local.database.TPosDetail;
import dev.iconpln.mims.data.local.database.TMaterialDetail;
import dev.iconpln.mims.data.local.database.TMaterialGroups;
import dev.iconpln.mims.data.local.database.TPrivilege;
import dev.iconpln.mims.data.local.database.TMaterial;
import dev.iconpln.mims.data.local.database.TPos;
import dev.iconpln.mims.data.local.database.TPosPenerimaan;
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan;
import dev.iconpln.mims.data.local.database.TPosSns;
import dev.iconpln.mims.data.local.database.TSnPermaterial;
import dev.iconpln.mims.data.local.database.TPemeriksaan;
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail;
import dev.iconpln.mims.data.local.database.TPhoto;
import dev.iconpln.mims.data.local.database.TRating;
import dev.iconpln.mims.data.local.database.TDataRating;
import dev.iconpln.mims.data.local.database.TTransDataRating;
import dev.iconpln.mims.data.local.database.TMonitoringPermintaan;
import dev.iconpln.mims.data.local.database.TMonitoringPermintaanDetail;
import dev.iconpln.mims.data.local.database.TTransMonitoringPermintaan;
import dev.iconpln.mims.data.local.database.TTransMonitoringPermintaanDetail;
import dev.iconpln.mims.data.local.database.TSnMonitoringPermintaan;
import dev.iconpln.mims.data.local.database.TMonitoringSnMaterial;
import dev.iconpln.mims.data.local.database.TPenerimaanUlp;
import dev.iconpln.mims.data.local.database.TPenerimaanDetailUlp;
import dev.iconpln.mims.data.local.database.TTransPenerimaanUlp;
import dev.iconpln.mims.data.local.database.TTransPenerimaanDetailUlp;
import dev.iconpln.mims.data.local.database.TListSnMaterialPenerimaanUlp;
import dev.iconpln.mims.data.local.database.TPemakaian;
import dev.iconpln.mims.data.local.database.TPemakaianDetail;
import dev.iconpln.mims.data.local.database.TTransPemakaianDetail;
import dev.iconpln.mims.data.local.database.TListSnMaterialPemakaianUlp;
import dev.iconpln.mims.data.local.database.TPetugasPengujian;
import dev.iconpln.mims.data.local.database.TMonitoringComplaint;
import dev.iconpln.mims.data.local.database.TMonitoringComplaintDetail;
import dev.iconpln.mims.data.local.database.TPegawaiUp3;

import dev.iconpln.mims.data.local.database.TMonitoringDao;
import dev.iconpln.mims.data.local.database.TPengujianDao;
import dev.iconpln.mims.data.local.database.TPengujianDetailsDao;
import dev.iconpln.mims.data.local.database.TLokasiDao;
import dev.iconpln.mims.data.local.database.TPosDetailDao;
import dev.iconpln.mims.data.local.database.TMaterialDetailDao;
import dev.iconpln.mims.data.local.database.TMaterialGroupsDao;
import dev.iconpln.mims.data.local.database.TPrivilegeDao;
import dev.iconpln.mims.data.local.database.TMaterialDao;
import dev.iconpln.mims.data.local.database.TPosDao;
import dev.iconpln.mims.data.local.database.TPosPenerimaanDao;
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaanDao;
import dev.iconpln.mims.data.local.database.TPosSnsDao;
import dev.iconpln.mims.data.local.database.TSnPermaterialDao;
import dev.iconpln.mims.data.local.database.TPemeriksaanDao;
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao;
import dev.iconpln.mims.data.local.database.TPhotoDao;
import dev.iconpln.mims.data.local.database.TRatingDao;
import dev.iconpln.mims.data.local.database.TDataRatingDao;
import dev.iconpln.mims.data.local.database.TTransDataRatingDao;
import dev.iconpln.mims.data.local.database.TMonitoringPermintaanDao;
import dev.iconpln.mims.data.local.database.TMonitoringPermintaanDetailDao;
import dev.iconpln.mims.data.local.database.TTransMonitoringPermintaanDao;
import dev.iconpln.mims.data.local.database.TTransMonitoringPermintaanDetailDao;
import dev.iconpln.mims.data.local.database.TSnMonitoringPermintaanDao;
import dev.iconpln.mims.data.local.database.TMonitoringSnMaterialDao;
import dev.iconpln.mims.data.local.database.TPenerimaanUlpDao;
import dev.iconpln.mims.data.local.database.TPenerimaanDetailUlpDao;
import dev.iconpln.mims.data.local.database.TTransPenerimaanUlpDao;
import dev.iconpln.mims.data.local.database.TTransPenerimaanDetailUlpDao;
import dev.iconpln.mims.data.local.database.TListSnMaterialPenerimaanUlpDao;
import dev.iconpln.mims.data.local.database.TPemakaianDao;
import dev.iconpln.mims.data.local.database.TPemakaianDetailDao;
import dev.iconpln.mims.data.local.database.TTransPemakaianDetailDao;
import dev.iconpln.mims.data.local.database.TListSnMaterialPemakaianUlpDao;
import dev.iconpln.mims.data.local.database.TPetugasPengujianDao;
import dev.iconpln.mims.data.local.database.TMonitoringComplaintDao;
import dev.iconpln.mims.data.local.database.TMonitoringComplaintDetailDao;
import dev.iconpln.mims.data.local.database.TPegawaiUp3Dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig tMonitoringDaoConfig;
    private final DaoConfig tPengujianDaoConfig;
    private final DaoConfig tPengujianDetailsDaoConfig;
    private final DaoConfig tLokasiDaoConfig;
    private final DaoConfig tPosDetailDaoConfig;
    private final DaoConfig tMaterialDetailDaoConfig;
    private final DaoConfig tMaterialGroupsDaoConfig;
    private final DaoConfig tPrivilegeDaoConfig;
    private final DaoConfig tMaterialDaoConfig;
    private final DaoConfig tPosDaoConfig;
    private final DaoConfig tPosPenerimaanDaoConfig;
    private final DaoConfig tPosDetailPenerimaanDaoConfig;
    private final DaoConfig tPosSnsDaoConfig;
    private final DaoConfig tSnPermaterialDaoConfig;
    private final DaoConfig tPemeriksaanDaoConfig;
    private final DaoConfig tPemeriksaanDetailDaoConfig;
    private final DaoConfig tPhotoDaoConfig;
    private final DaoConfig tRatingDaoConfig;
    private final DaoConfig tDataRatingDaoConfig;
    private final DaoConfig tTransDataRatingDaoConfig;
    private final DaoConfig tMonitoringPermintaanDaoConfig;
    private final DaoConfig tMonitoringPermintaanDetailDaoConfig;
    private final DaoConfig tTransMonitoringPermintaanDaoConfig;
    private final DaoConfig tTransMonitoringPermintaanDetailDaoConfig;
    private final DaoConfig tSnMonitoringPermintaanDaoConfig;
    private final DaoConfig tMonitoringSnMaterialDaoConfig;
    private final DaoConfig tPenerimaanUlpDaoConfig;
    private final DaoConfig tPenerimaanDetailUlpDaoConfig;
    private final DaoConfig tTransPenerimaanUlpDaoConfig;
    private final DaoConfig tTransPenerimaanDetailUlpDaoConfig;
    private final DaoConfig tListSnMaterialPenerimaanUlpDaoConfig;
    private final DaoConfig tPemakaianDaoConfig;
    private final DaoConfig tPemakaianDetailDaoConfig;
    private final DaoConfig tTransPemakaianDetailDaoConfig;
    private final DaoConfig tListSnMaterialPemakaianUlpDaoConfig;
    private final DaoConfig tPetugasPengujianDaoConfig;
    private final DaoConfig tMonitoringComplaintDaoConfig;
    private final DaoConfig tMonitoringComplaintDetailDaoConfig;
    private final DaoConfig tPegawaiUp3DaoConfig;

    private final TMonitoringDao tMonitoringDao;
    private final TPengujianDao tPengujianDao;
    private final TPengujianDetailsDao tPengujianDetailsDao;
    private final TLokasiDao tLokasiDao;
    private final TPosDetailDao tPosDetailDao;
    private final TMaterialDetailDao tMaterialDetailDao;
    private final TMaterialGroupsDao tMaterialGroupsDao;
    private final TPrivilegeDao tPrivilegeDao;
    private final TMaterialDao tMaterialDao;
    private final TPosDao tPosDao;
    private final TPosPenerimaanDao tPosPenerimaanDao;
    private final TPosDetailPenerimaanDao tPosDetailPenerimaanDao;
    private final TPosSnsDao tPosSnsDao;
    private final TSnPermaterialDao tSnPermaterialDao;
    private final TPemeriksaanDao tPemeriksaanDao;
    private final TPemeriksaanDetailDao tPemeriksaanDetailDao;
    private final TPhotoDao tPhotoDao;
    private final TRatingDao tRatingDao;
    private final TDataRatingDao tDataRatingDao;
    private final TTransDataRatingDao tTransDataRatingDao;
    private final TMonitoringPermintaanDao tMonitoringPermintaanDao;
    private final TMonitoringPermintaanDetailDao tMonitoringPermintaanDetailDao;
    private final TTransMonitoringPermintaanDao tTransMonitoringPermintaanDao;
    private final TTransMonitoringPermintaanDetailDao tTransMonitoringPermintaanDetailDao;
    private final TSnMonitoringPermintaanDao tSnMonitoringPermintaanDao;
    private final TMonitoringSnMaterialDao tMonitoringSnMaterialDao;
    private final TPenerimaanUlpDao tPenerimaanUlpDao;
    private final TPenerimaanDetailUlpDao tPenerimaanDetailUlpDao;
    private final TTransPenerimaanUlpDao tTransPenerimaanUlpDao;
    private final TTransPenerimaanDetailUlpDao tTransPenerimaanDetailUlpDao;
    private final TListSnMaterialPenerimaanUlpDao tListSnMaterialPenerimaanUlpDao;
    private final TPemakaianDao tPemakaianDao;
    private final TPemakaianDetailDao tPemakaianDetailDao;
    private final TTransPemakaianDetailDao tTransPemakaianDetailDao;
    private final TListSnMaterialPemakaianUlpDao tListSnMaterialPemakaianUlpDao;
    private final TPetugasPengujianDao tPetugasPengujianDao;
    private final TMonitoringComplaintDao tMonitoringComplaintDao;
    private final TMonitoringComplaintDetailDao tMonitoringComplaintDetailDao;
    private final TPegawaiUp3Dao tPegawaiUp3Dao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        tMonitoringDaoConfig = daoConfigMap.get(TMonitoringDao.class).clone();
        tMonitoringDaoConfig.initIdentityScope(type);

        tPengujianDaoConfig = daoConfigMap.get(TPengujianDao.class).clone();
        tPengujianDaoConfig.initIdentityScope(type);

        tPengujianDetailsDaoConfig = daoConfigMap.get(TPengujianDetailsDao.class).clone();
        tPengujianDetailsDaoConfig.initIdentityScope(type);

        tLokasiDaoConfig = daoConfigMap.get(TLokasiDao.class).clone();
        tLokasiDaoConfig.initIdentityScope(type);

        tPosDetailDaoConfig = daoConfigMap.get(TPosDetailDao.class).clone();
        tPosDetailDaoConfig.initIdentityScope(type);

        tMaterialDetailDaoConfig = daoConfigMap.get(TMaterialDetailDao.class).clone();
        tMaterialDetailDaoConfig.initIdentityScope(type);

        tMaterialGroupsDaoConfig = daoConfigMap.get(TMaterialGroupsDao.class).clone();
        tMaterialGroupsDaoConfig.initIdentityScope(type);

        tPrivilegeDaoConfig = daoConfigMap.get(TPrivilegeDao.class).clone();
        tPrivilegeDaoConfig.initIdentityScope(type);

        tMaterialDaoConfig = daoConfigMap.get(TMaterialDao.class).clone();
        tMaterialDaoConfig.initIdentityScope(type);

        tPosDaoConfig = daoConfigMap.get(TPosDao.class).clone();
        tPosDaoConfig.initIdentityScope(type);

        tPosPenerimaanDaoConfig = daoConfigMap.get(TPosPenerimaanDao.class).clone();
        tPosPenerimaanDaoConfig.initIdentityScope(type);

        tPosDetailPenerimaanDaoConfig = daoConfigMap.get(TPosDetailPenerimaanDao.class).clone();
        tPosDetailPenerimaanDaoConfig.initIdentityScope(type);

        tPosSnsDaoConfig = daoConfigMap.get(TPosSnsDao.class).clone();
        tPosSnsDaoConfig.initIdentityScope(type);

        tSnPermaterialDaoConfig = daoConfigMap.get(TSnPermaterialDao.class).clone();
        tSnPermaterialDaoConfig.initIdentityScope(type);

        tPemeriksaanDaoConfig = daoConfigMap.get(TPemeriksaanDao.class).clone();
        tPemeriksaanDaoConfig.initIdentityScope(type);

        tPemeriksaanDetailDaoConfig = daoConfigMap.get(TPemeriksaanDetailDao.class).clone();
        tPemeriksaanDetailDaoConfig.initIdentityScope(type);

        tPhotoDaoConfig = daoConfigMap.get(TPhotoDao.class).clone();
        tPhotoDaoConfig.initIdentityScope(type);

        tRatingDaoConfig = daoConfigMap.get(TRatingDao.class).clone();
        tRatingDaoConfig.initIdentityScope(type);

        tDataRatingDaoConfig = daoConfigMap.get(TDataRatingDao.class).clone();
        tDataRatingDaoConfig.initIdentityScope(type);

        tTransDataRatingDaoConfig = daoConfigMap.get(TTransDataRatingDao.class).clone();
        tTransDataRatingDaoConfig.initIdentityScope(type);

        tMonitoringPermintaanDaoConfig = daoConfigMap.get(TMonitoringPermintaanDao.class).clone();
        tMonitoringPermintaanDaoConfig.initIdentityScope(type);

        tMonitoringPermintaanDetailDaoConfig = daoConfigMap.get(TMonitoringPermintaanDetailDao.class).clone();
        tMonitoringPermintaanDetailDaoConfig.initIdentityScope(type);

        tTransMonitoringPermintaanDaoConfig = daoConfigMap.get(TTransMonitoringPermintaanDao.class).clone();
        tTransMonitoringPermintaanDaoConfig.initIdentityScope(type);

        tTransMonitoringPermintaanDetailDaoConfig = daoConfigMap.get(TTransMonitoringPermintaanDetailDao.class).clone();
        tTransMonitoringPermintaanDetailDaoConfig.initIdentityScope(type);

        tSnMonitoringPermintaanDaoConfig = daoConfigMap.get(TSnMonitoringPermintaanDao.class).clone();
        tSnMonitoringPermintaanDaoConfig.initIdentityScope(type);

        tMonitoringSnMaterialDaoConfig = daoConfigMap.get(TMonitoringSnMaterialDao.class).clone();
        tMonitoringSnMaterialDaoConfig.initIdentityScope(type);

        tPenerimaanUlpDaoConfig = daoConfigMap.get(TPenerimaanUlpDao.class).clone();
        tPenerimaanUlpDaoConfig.initIdentityScope(type);

        tPenerimaanDetailUlpDaoConfig = daoConfigMap.get(TPenerimaanDetailUlpDao.class).clone();
        tPenerimaanDetailUlpDaoConfig.initIdentityScope(type);

        tTransPenerimaanUlpDaoConfig = daoConfigMap.get(TTransPenerimaanUlpDao.class).clone();
        tTransPenerimaanUlpDaoConfig.initIdentityScope(type);

        tTransPenerimaanDetailUlpDaoConfig = daoConfigMap.get(TTransPenerimaanDetailUlpDao.class).clone();
        tTransPenerimaanDetailUlpDaoConfig.initIdentityScope(type);

        tListSnMaterialPenerimaanUlpDaoConfig = daoConfigMap.get(TListSnMaterialPenerimaanUlpDao.class).clone();
        tListSnMaterialPenerimaanUlpDaoConfig.initIdentityScope(type);

        tPemakaianDaoConfig = daoConfigMap.get(TPemakaianDao.class).clone();
        tPemakaianDaoConfig.initIdentityScope(type);

        tPemakaianDetailDaoConfig = daoConfigMap.get(TPemakaianDetailDao.class).clone();
        tPemakaianDetailDaoConfig.initIdentityScope(type);

        tTransPemakaianDetailDaoConfig = daoConfigMap.get(TTransPemakaianDetailDao.class).clone();
        tTransPemakaianDetailDaoConfig.initIdentityScope(type);

        tListSnMaterialPemakaianUlpDaoConfig = daoConfigMap.get(TListSnMaterialPemakaianUlpDao.class).clone();
        tListSnMaterialPemakaianUlpDaoConfig.initIdentityScope(type);

        tPetugasPengujianDaoConfig = daoConfigMap.get(TPetugasPengujianDao.class).clone();
        tPetugasPengujianDaoConfig.initIdentityScope(type);

        tMonitoringComplaintDaoConfig = daoConfigMap.get(TMonitoringComplaintDao.class).clone();
        tMonitoringComplaintDaoConfig.initIdentityScope(type);

        tMonitoringComplaintDetailDaoConfig = daoConfigMap.get(TMonitoringComplaintDetailDao.class).clone();
        tMonitoringComplaintDetailDaoConfig.initIdentityScope(type);

        tPegawaiUp3DaoConfig = daoConfigMap.get(TPegawaiUp3Dao.class).clone();
        tPegawaiUp3DaoConfig.initIdentityScope(type);

        tMonitoringDao = new TMonitoringDao(tMonitoringDaoConfig, this);
        tPengujianDao = new TPengujianDao(tPengujianDaoConfig, this);
        tPengujianDetailsDao = new TPengujianDetailsDao(tPengujianDetailsDaoConfig, this);
        tLokasiDao = new TLokasiDao(tLokasiDaoConfig, this);
        tPosDetailDao = new TPosDetailDao(tPosDetailDaoConfig, this);
        tMaterialDetailDao = new TMaterialDetailDao(tMaterialDetailDaoConfig, this);
        tMaterialGroupsDao = new TMaterialGroupsDao(tMaterialGroupsDaoConfig, this);
        tPrivilegeDao = new TPrivilegeDao(tPrivilegeDaoConfig, this);
        tMaterialDao = new TMaterialDao(tMaterialDaoConfig, this);
        tPosDao = new TPosDao(tPosDaoConfig, this);
        tPosPenerimaanDao = new TPosPenerimaanDao(tPosPenerimaanDaoConfig, this);
        tPosDetailPenerimaanDao = new TPosDetailPenerimaanDao(tPosDetailPenerimaanDaoConfig, this);
        tPosSnsDao = new TPosSnsDao(tPosSnsDaoConfig, this);
        tSnPermaterialDao = new TSnPermaterialDao(tSnPermaterialDaoConfig, this);
        tPemeriksaanDao = new TPemeriksaanDao(tPemeriksaanDaoConfig, this);
        tPemeriksaanDetailDao = new TPemeriksaanDetailDao(tPemeriksaanDetailDaoConfig, this);
        tPhotoDao = new TPhotoDao(tPhotoDaoConfig, this);
        tRatingDao = new TRatingDao(tRatingDaoConfig, this);
        tDataRatingDao = new TDataRatingDao(tDataRatingDaoConfig, this);
        tTransDataRatingDao = new TTransDataRatingDao(tTransDataRatingDaoConfig, this);
        tMonitoringPermintaanDao = new TMonitoringPermintaanDao(tMonitoringPermintaanDaoConfig, this);
        tMonitoringPermintaanDetailDao = new TMonitoringPermintaanDetailDao(tMonitoringPermintaanDetailDaoConfig, this);
        tTransMonitoringPermintaanDao = new TTransMonitoringPermintaanDao(tTransMonitoringPermintaanDaoConfig, this);
        tTransMonitoringPermintaanDetailDao = new TTransMonitoringPermintaanDetailDao(tTransMonitoringPermintaanDetailDaoConfig, this);
        tSnMonitoringPermintaanDao = new TSnMonitoringPermintaanDao(tSnMonitoringPermintaanDaoConfig, this);
        tMonitoringSnMaterialDao = new TMonitoringSnMaterialDao(tMonitoringSnMaterialDaoConfig, this);
        tPenerimaanUlpDao = new TPenerimaanUlpDao(tPenerimaanUlpDaoConfig, this);
        tPenerimaanDetailUlpDao = new TPenerimaanDetailUlpDao(tPenerimaanDetailUlpDaoConfig, this);
        tTransPenerimaanUlpDao = new TTransPenerimaanUlpDao(tTransPenerimaanUlpDaoConfig, this);
        tTransPenerimaanDetailUlpDao = new TTransPenerimaanDetailUlpDao(tTransPenerimaanDetailUlpDaoConfig, this);
        tListSnMaterialPenerimaanUlpDao = new TListSnMaterialPenerimaanUlpDao(tListSnMaterialPenerimaanUlpDaoConfig, this);
        tPemakaianDao = new TPemakaianDao(tPemakaianDaoConfig, this);
        tPemakaianDetailDao = new TPemakaianDetailDao(tPemakaianDetailDaoConfig, this);
        tTransPemakaianDetailDao = new TTransPemakaianDetailDao(tTransPemakaianDetailDaoConfig, this);
        tListSnMaterialPemakaianUlpDao = new TListSnMaterialPemakaianUlpDao(tListSnMaterialPemakaianUlpDaoConfig, this);
        tPetugasPengujianDao = new TPetugasPengujianDao(tPetugasPengujianDaoConfig, this);
        tMonitoringComplaintDao = new TMonitoringComplaintDao(tMonitoringComplaintDaoConfig, this);
        tMonitoringComplaintDetailDao = new TMonitoringComplaintDetailDao(tMonitoringComplaintDetailDaoConfig, this);
        tPegawaiUp3Dao = new TPegawaiUp3Dao(tPegawaiUp3DaoConfig, this);

        registerDao(TMonitoring.class, tMonitoringDao);
        registerDao(TPengujian.class, tPengujianDao);
        registerDao(TPengujianDetails.class, tPengujianDetailsDao);
        registerDao(TLokasi.class, tLokasiDao);
        registerDao(TPosDetail.class, tPosDetailDao);
        registerDao(TMaterialDetail.class, tMaterialDetailDao);
        registerDao(TMaterialGroups.class, tMaterialGroupsDao);
        registerDao(TPrivilege.class, tPrivilegeDao);
        registerDao(TMaterial.class, tMaterialDao);
        registerDao(TPos.class, tPosDao);
        registerDao(TPosPenerimaan.class, tPosPenerimaanDao);
        registerDao(TPosDetailPenerimaan.class, tPosDetailPenerimaanDao);
        registerDao(TPosSns.class, tPosSnsDao);
        registerDao(TSnPermaterial.class, tSnPermaterialDao);
        registerDao(TPemeriksaan.class, tPemeriksaanDao);
        registerDao(TPemeriksaanDetail.class, tPemeriksaanDetailDao);
        registerDao(TPhoto.class, tPhotoDao);
        registerDao(TRating.class, tRatingDao);
        registerDao(TDataRating.class, tDataRatingDao);
        registerDao(TTransDataRating.class, tTransDataRatingDao);
        registerDao(TMonitoringPermintaan.class, tMonitoringPermintaanDao);
        registerDao(TMonitoringPermintaanDetail.class, tMonitoringPermintaanDetailDao);
        registerDao(TTransMonitoringPermintaan.class, tTransMonitoringPermintaanDao);
        registerDao(TTransMonitoringPermintaanDetail.class, tTransMonitoringPermintaanDetailDao);
        registerDao(TSnMonitoringPermintaan.class, tSnMonitoringPermintaanDao);
        registerDao(TMonitoringSnMaterial.class, tMonitoringSnMaterialDao);
        registerDao(TPenerimaanUlp.class, tPenerimaanUlpDao);
        registerDao(TPenerimaanDetailUlp.class, tPenerimaanDetailUlpDao);
        registerDao(TTransPenerimaanUlp.class, tTransPenerimaanUlpDao);
        registerDao(TTransPenerimaanDetailUlp.class, tTransPenerimaanDetailUlpDao);
        registerDao(TListSnMaterialPenerimaanUlp.class, tListSnMaterialPenerimaanUlpDao);
        registerDao(TPemakaian.class, tPemakaianDao);
        registerDao(TPemakaianDetail.class, tPemakaianDetailDao);
        registerDao(TTransPemakaianDetail.class, tTransPemakaianDetailDao);
        registerDao(TListSnMaterialPemakaianUlp.class, tListSnMaterialPemakaianUlpDao);
        registerDao(TPetugasPengujian.class, tPetugasPengujianDao);
        registerDao(TMonitoringComplaint.class, tMonitoringComplaintDao);
        registerDao(TMonitoringComplaintDetail.class, tMonitoringComplaintDetailDao);
        registerDao(TPegawaiUp3.class, tPegawaiUp3Dao);
    }
    
    public void clear() {
        tMonitoringDaoConfig.clearIdentityScope();
        tPengujianDaoConfig.clearIdentityScope();
        tPengujianDetailsDaoConfig.clearIdentityScope();
        tLokasiDaoConfig.clearIdentityScope();
        tPosDetailDaoConfig.clearIdentityScope();
        tMaterialDetailDaoConfig.clearIdentityScope();
        tMaterialGroupsDaoConfig.clearIdentityScope();
        tPrivilegeDaoConfig.clearIdentityScope();
        tMaterialDaoConfig.clearIdentityScope();
        tPosDaoConfig.clearIdentityScope();
        tPosPenerimaanDaoConfig.clearIdentityScope();
        tPosDetailPenerimaanDaoConfig.clearIdentityScope();
        tPosSnsDaoConfig.clearIdentityScope();
        tSnPermaterialDaoConfig.clearIdentityScope();
        tPemeriksaanDaoConfig.clearIdentityScope();
        tPemeriksaanDetailDaoConfig.clearIdentityScope();
        tPhotoDaoConfig.clearIdentityScope();
        tRatingDaoConfig.clearIdentityScope();
        tDataRatingDaoConfig.clearIdentityScope();
        tTransDataRatingDaoConfig.clearIdentityScope();
        tMonitoringPermintaanDaoConfig.clearIdentityScope();
        tMonitoringPermintaanDetailDaoConfig.clearIdentityScope();
        tTransMonitoringPermintaanDaoConfig.clearIdentityScope();
        tTransMonitoringPermintaanDetailDaoConfig.clearIdentityScope();
        tSnMonitoringPermintaanDaoConfig.clearIdentityScope();
        tMonitoringSnMaterialDaoConfig.clearIdentityScope();
        tPenerimaanUlpDaoConfig.clearIdentityScope();
        tPenerimaanDetailUlpDaoConfig.clearIdentityScope();
        tTransPenerimaanUlpDaoConfig.clearIdentityScope();
        tTransPenerimaanDetailUlpDaoConfig.clearIdentityScope();
        tListSnMaterialPenerimaanUlpDaoConfig.clearIdentityScope();
        tPemakaianDaoConfig.clearIdentityScope();
        tPemakaianDetailDaoConfig.clearIdentityScope();
        tTransPemakaianDetailDaoConfig.clearIdentityScope();
        tListSnMaterialPemakaianUlpDaoConfig.clearIdentityScope();
        tPetugasPengujianDaoConfig.clearIdentityScope();
        tMonitoringComplaintDaoConfig.clearIdentityScope();
        tMonitoringComplaintDetailDaoConfig.clearIdentityScope();
        tPegawaiUp3DaoConfig.clearIdentityScope();
    }

    public TMonitoringDao getTMonitoringDao() {
        return tMonitoringDao;
    }

    public TPengujianDao getTPengujianDao() {
        return tPengujianDao;
    }

    public TPengujianDetailsDao getTPengujianDetailsDao() {
        return tPengujianDetailsDao;
    }

    public TLokasiDao getTLokasiDao() {
        return tLokasiDao;
    }

    public TPosDetailDao getTPosDetailDao() {
        return tPosDetailDao;
    }

    public TMaterialDetailDao getTMaterialDetailDao() {
        return tMaterialDetailDao;
    }

    public TMaterialGroupsDao getTMaterialGroupsDao() {
        return tMaterialGroupsDao;
    }

    public TPrivilegeDao getTPrivilegeDao() {
        return tPrivilegeDao;
    }

    public TMaterialDao getTMaterialDao() {
        return tMaterialDao;
    }

    public TPosDao getTPosDao() {
        return tPosDao;
    }

    public TPosPenerimaanDao getTPosPenerimaanDao() {
        return tPosPenerimaanDao;
    }

    public TPosDetailPenerimaanDao getTPosDetailPenerimaanDao() {
        return tPosDetailPenerimaanDao;
    }

    public TPosSnsDao getTPosSnsDao() {
        return tPosSnsDao;
    }

    public TSnPermaterialDao getTSnPermaterialDao() {
        return tSnPermaterialDao;
    }

    public TPemeriksaanDao getTPemeriksaanDao() {
        return tPemeriksaanDao;
    }

    public TPemeriksaanDetailDao getTPemeriksaanDetailDao() {
        return tPemeriksaanDetailDao;
    }

    public TPhotoDao getTPhotoDao() {
        return tPhotoDao;
    }

    public TRatingDao getTRatingDao() {
        return tRatingDao;
    }

    public TDataRatingDao getTDataRatingDao() {
        return tDataRatingDao;
    }

    public TTransDataRatingDao getTTransDataRatingDao() {
        return tTransDataRatingDao;
    }

    public TMonitoringPermintaanDao getTMonitoringPermintaanDao() {
        return tMonitoringPermintaanDao;
    }

    public TMonitoringPermintaanDetailDao getTMonitoringPermintaanDetailDao() {
        return tMonitoringPermintaanDetailDao;
    }

    public TTransMonitoringPermintaanDao getTTransMonitoringPermintaanDao() {
        return tTransMonitoringPermintaanDao;
    }

    public TTransMonitoringPermintaanDetailDao getTTransMonitoringPermintaanDetailDao() {
        return tTransMonitoringPermintaanDetailDao;
    }

    public TSnMonitoringPermintaanDao getTSnMonitoringPermintaanDao() {
        return tSnMonitoringPermintaanDao;
    }

    public TMonitoringSnMaterialDao getTMonitoringSnMaterialDao() {
        return tMonitoringSnMaterialDao;
    }

    public TPenerimaanUlpDao getTPenerimaanUlpDao() {
        return tPenerimaanUlpDao;
    }

    public TPenerimaanDetailUlpDao getTPenerimaanDetailUlpDao() {
        return tPenerimaanDetailUlpDao;
    }

    public TTransPenerimaanUlpDao getTTransPenerimaanUlpDao() {
        return tTransPenerimaanUlpDao;
    }

    public TTransPenerimaanDetailUlpDao getTTransPenerimaanDetailUlpDao() {
        return tTransPenerimaanDetailUlpDao;
    }

    public TListSnMaterialPenerimaanUlpDao getTListSnMaterialPenerimaanUlpDao() {
        return tListSnMaterialPenerimaanUlpDao;
    }

    public TPemakaianDao getTPemakaianDao() {
        return tPemakaianDao;
    }

    public TPemakaianDetailDao getTPemakaianDetailDao() {
        return tPemakaianDetailDao;
    }

    public TTransPemakaianDetailDao getTTransPemakaianDetailDao() {
        return tTransPemakaianDetailDao;
    }

    public TListSnMaterialPemakaianUlpDao getTListSnMaterialPemakaianUlpDao() {
        return tListSnMaterialPemakaianUlpDao;
    }

    public TPetugasPengujianDao getTPetugasPengujianDao() {
        return tPetugasPengujianDao;
    }

    public TMonitoringComplaintDao getTMonitoringComplaintDao() {
        return tMonitoringComplaintDao;
    }

    public TMonitoringComplaintDetailDao getTMonitoringComplaintDetailDao() {
        return tMonitoringComplaintDetailDao;
    }

    public TPegawaiUp3Dao getTPegawaiUp3Dao() {
        return tPegawaiUp3Dao;
    }

}
