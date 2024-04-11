/**
 * Class that implements the general management of advertisements.
 *
 * @Author Fabio Palladino
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University of Salerno
 */
package unisa.gps.etour.control.AdvertisementManagement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import unisa.gps.etour.bean.BannerBean;
import unisa.gps.etour.repository.DBBanner;
//import unisa.gps.etour.control.AdvertisementManagement.test.stubs.*;
import unisa.gps.etour.repository.IBannerRepository;
import unisa.gps.etour.util.DataValidation;
import unisa.gps.etour.util.GlobalConstants;
import unisa.gps.etour.util.ErrorMessages;

public class AdvertisementManagement extends UnicastRemoteObject implements IAdvertisementManagement {
    private static final long serialVersionUID = 1L;
    private IBannerRepository bannerRepository;

    /**
     * Constructor, instantiates an object of type DBBanner that contains methods
     * for performing operations on banner entities.
     *
     * @throws RemoteException
     */
    public AdvertisementManagement() throws RemoteException {
        super();
        bannerRepository = new DBBanner();
    }

    /**
     * Delete a banner and the associated image from the system.
     *
     * @param bannerID id of the banner to remove
     * @throws RemoteException
     */
    public boolean deleteBanner(int bannerID) throws RemoteException {
        BannerBean banner;

        try {
            banner = bannerRepository.getBannerByID(bannerID);
            if (!DataValidation.checkBannerBean(banner)) {
                throw new RemoteException(ErrorMessages.DATA_ERROR);
            }
            File imageFile = new File(banner.getImagePath());
            imageFile.delete();
            return bannerRepository.deleteBanner(bannerID);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_CONNECTION_ERROR);
        }
    }

    /**
     * Method to change the image associated with the banner: Deletes the old image
     * and stores the new image at the same path as the old image.
     *
     * @param bannerID  id of the banner
     * @param newImage  ImageIcon object containing the new image of the banner
     * @return true if the operation is successful
     */
    public boolean modifyBanner(int bannerID, ImageIcon newImage) throws RemoteException {
        try {
            if (!DataValidation.checkImage(newImage)) {
                throw new RemoteException(ErrorMessages.DATA_ERROR);
            }
            BufferedImage bufferedImage = (BufferedImage) newImage.getImage();
            BannerBean banner = bannerRepository.getBannerByID(bannerID);
            if (!DataValidation.checkBannerBean(banner)) {
                throw new RemoteException(ErrorMessages.DATA_ERROR);
            }
            File imageFile = new File(banner.getImagePath());
            return ImageIO.write(bufferedImage, "jpg", imageFile);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_CONNECTION_ERROR);
        } catch (IOException e) {
            throw new RemoteException(ErrorMessages.FILE_ERROR);
        }
    }
}

/**
 * Returns a list of banners for a refreshment.
 *
 * @param pRefreshmentID ID of the refreshment
 * @throws RemoteException
 */
public HashMap<BannerBean, ImageIcon> getBannersByID(int pRefreshmentID) throws RemoteException {
    HashMap<BannerBean, ImageIcon> toReturn = new HashMap<>();

    try {
        ArrayList<BannerBean> bannerList = bannerRepository.getBanners(pRefreshmentID);
        for (BannerBean banner : bannerList) {
            if (DataValidation.checkBannerBean(banner)) {
                File imageFile = new File(banner.getImagePath());
                ImageIcon icon = new ImageIcon(ImageIO.read(imageFile));
                toReturn.put(banner, icon);
            }
        }
        return toReturn;
    } catch (SQLException e) {
        throw new RemoteException(ErrorMessages.DB_CONNECTION_ERROR);
    } catch (IOException e) {
        throw new RemoteException(ErrorMessages.FILE_ERROR);
    } catch (Exception e) {
        throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
    }
}
/**
 * Create and insert a new banner, ensuring that the maximum number
 * of banners displayed for a refreshment point is not exceeded.
 *
 * @param pRefreshmentID   ID of the refreshment
 * @param pBannerImage     ImageIcon object containing the banner image
 * @return Returns true if the insertion has been successful.
 * @throws RemoteException
 */
public boolean insertBanner(int pRefreshmentID, ImageIcon pBannerImage) throws RemoteException {
    try {
        if (!DataValidation.checkImage(pBannerImage)) {
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        }
        IDBConvention dbConvention = new DBConvention();
        BeanConvention convention = dbConvention.getActiveConvention(pRefreshmentID);
        int maxBanner = convention.getMaxBanner();
        int numBanner = bannerRepository.getBanners(pRefreshmentID).size();
        if (!(numBanner < maxBanner)) {
            throw new Exception(ErrorMessages.MAX_BANNER_REACHED);
        }
        BannerBean banner = new BannerBean();
        String path = GlobalConstants.SERVER_IMAGE_PATH + pRefreshmentID;
        int i = 0;
        File imageFile = new File(path + "_" + i + ".jpg");
        while (imageFile.exists()) {
            i++;
            imageFile = new File(path + "_" + i + ".jpg");
        }
        BufferedImage image = (BufferedImage) pBannerImage.getImage();
        if (!ImageIO.write(image, "jpg", imageFile)) {
            throw new IOException();
        }
        banner.setRefreshmentID(pRefreshmentID);
        banner.setImagePath(imageFile.getPath());
        return bannerRepository.insertBanner(banner);
    } catch (SQLException e) {
        throw new RemoteException(ErrorMessages.DB_CONNECTION_ERROR);
    } catch (IOException e) {
        throw new RemoteException(ErrorMessages.FILE_ERROR);
    } catch (Exception e) {
        if (e.getMessage().equals(ErrorMessages.MAX_BANNER_REACHED)) {
            throw new RemoteException(ErrorMessages.MAX_BANNER_REACHED);
        } else {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
}
