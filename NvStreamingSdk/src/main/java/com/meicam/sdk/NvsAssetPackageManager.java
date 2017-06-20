//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 7. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import java.util.List;

/*!
 *  \brief 资源包管理器，管理视频场景中的资源包

 *   在SDK开发过中，资源包管理器统一对需要的各种特技资源包包括字幕，主题，动画贴纸等进行相应的安装，升级，卸载等操作。在安装，升级，卸载时，出现差错都会有相应的错误提示类型，以便快速定位和解决错误。
 */
public class NvsAssetPackageManager
{
    /*!
     *  \brief 资源包管理回调接口
     */
    public interface AssetPackageManagerCallback
    {
        /*!
         *  \brief 通知资源包的安装过程结束
         *  \param assetPackageId 安装资源包ID
         *  \param assetPackageFilePath 安装资源包的文件路径
         *  \param assetPackageType 安装资源包的类型。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
         *  \param error 安装过程的错误返回值，为NvsAssetPackageManagerError_NoError表示安装成功，否则表示安装失败。请参见[资源包管理错误类型](@ref ASSET_PACKAGE_MANAGER_ERROR)
         */
        void onFinishAssetPackageInstallation(String assetPackageId, String assetPackageFilePath, int assetPackageType, int error);

        /*!
         *  \brief 通知资源包的升级过程结束
         *  \param assetPackageId 升级资源包ID
         *  \param assetPackageFilePath 升级资源包的文件路径
         *  \param assetPackageType 升级资源包的类型。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
         *  \param error 升级过程的错误返回值，为NvsAssetPackageManagerError_NoError表示安装成功，否则表示安装失败。请参见[资源包管理错误类型](@ref ASSET_PACKAGE_MANAGER_ERROR)
         */
        void onFinishAssetPackageUpgrading(String assetPackageId, String assetPackageFilePath, int assetPackageType, int error);
    }

/*! \anchor ASSET_PACKAGE_TYPE */
/*! @name 资源包类型 */
//!@{
    public static final int ASSET_PACKAGE_TYPE_VIDEOFX = 0;                      //!< \if ENGLISH \else 视频特效类型 \endif
    public static final int ASSET_PACKAGE_TYPE_VIDEOTRANSITION = 1;              //!< \if ENGLISH \else 视频转场类型 \endif
    public static final int ASSET_PACKAGE_TYPE_CAPTIONSTYLE = 2;                 //!< \if ENGLISH \else 字幕样式类型 \endif
    public static final int ASSET_PACKAGE_TYPE_ANIMATEDSTICKER = 3;              //!< \if ENGLISH \else 动画贴纸类型 \endif
    public static final int ASSET_PACKAGE_TYPE_THEME = 4;                        //!< \if ENGLISH \else 主题类型 \endif
    public static final int ASSET_PACKAGE_TYPE_CAPTURESCENE = 5;                 //!< \if ENGLISH \else 采集场景类型 \endif

//!@}

/*! \anchor ASSET_PACKAGE_STATUS */
/*! @name 资源包状态 */
//!@{
    public static final int ASSET_PACKAGE_STATUS_NOTINSTALLED = 0;               //!< \if ENGLISH \else 未安装状态 \endif
    public static final int ASSET_PACKAGE_STATUS_INSTALLING = 1;                 //!< \if ENGLISH \else 正在安装状态 \endif
    public static final int ASSET_PACKAGE_STATUS_READY = 2;                      //!< \if ENGLISH \else 准备状态 \endif
    public static final int ASSET_PACKAGE_STATUS_UPGRADING = 3;                  //!< \if ENGLISH \else 正在升级状态 \endif

//!@}

/*! \anchor ASSET_PACKAGE_MANAGER_ERROR */
/*! @name 资源包管理错误类型 */
//!@{
    public static final int ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR = 0;            //!< \if ENGLISH \else 安装成功 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_NAME = 1;                //!< \if ENGLISH \else 名称错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED = 2;   //!< \if ENGLISH \else 已经安装 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_WORKING_INPROGRESS = 3;  //!< \if ENGLISH \else 安装或升级正在进行中 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_NOT_INSTALLED = 4;       //!< \if ENGLISH \else 尚未安装 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_IMPROPER_STATUS = 5;     //!< \if ENGLISH \else 不正确状态 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_DECOMPRESSION = 6;       //!< \if ENGLISH \else 解压错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_INVALID_PACKAGE = 7;     //!< \if ENGLISH \else 无效包 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_ASSET_TYPE = 8;          //!< \if ENGLISH \else 资源类型错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_PERMISSION = 9;          //!< \if ENGLISH \else 许可错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_META_CONTENT = 10;       //!< \if ENGLISH \else 元内容错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_SDK_VERSION = 11;        //!< \if ENGLISH \else SDK版本错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_UPGRADE_VERSION = 12;    //!< \if ENGLISH \else 升级版本错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_IO = 13;                 //!< \if ENGLISH \else 输入输出错误 \endif
    public static final int ASSET_PACKAGE_MANAGER_ERROR_RESOURCE = 14;           //!< \if ENGLISH \else 资源错误 \endif

//!@}

    long m_internalObject = 0;
    long m_internalCallbackObject = 0;
    AssetPackageManagerCallback m_callback = null;

    NvsAssetPackageManager()
    {
        nativeSetInternalCallbackObject();
    }

    void setInternalObject(long internalObject)
    {
        if (m_internalObject != 0)
            setCallbackInterface(null);
        m_internalObject = internalObject;
    }

    /*!
        \brief 设置包管理器回调接口
        \param callback 包管理器回调接口
    */
    public void setCallbackInterface(AssetPackageManagerCallback callback)
    {
        m_callback = callback;
        nativeSetCallbackInterface(m_callback);
    }

    /*!
     *  \brief 从资源包的文件路径获得资源包的ID
     *  \param assetPackageFilePath 资源包的文件路径
     *  \return 返回表示资源包ID的字符串
     */
    public String getAssetPackageIdFromAssetPackageFilePath(String assetPackageFilePath)
    {
        return nativeGetAssetPackageIdFromAssetPackageFilePath(m_internalObject, assetPackageFilePath);
    }

    /*!
     *  \brief 安装资源包
     *  \param assetPackageFilePath 待安装资源包的文件路径
     *  \param licenseFilePath 待安装资源包的授权文件路径
     *  \param type 待安装资源包的类型。注：待输入参数是以ASSET_PACKAGE_TYPE打头的静态int属性值。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
     *  \param synchronous 是否同步安装。值为true则安装过程会阻塞当前线程直到安装成功或者失败，false则安装过程的结果通过delegate异步通知（但也在当前线程通知）。
     *  \param assetPackageId 输出参数，返回该资源包ID
     *  \return 返回表示资源包错误类型的整形值。
     *
     *  注意：返回值是ASSET_PACKAGE_MANAGER_ERROR打头的静态int属性值。若返回ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR: 表示安装成功（只有同步安装才可能返回这个)，任何其它返回值都表示安装错误。请参见[资源包管理错误类型](@ref ASSET_PACKAGE_MANAGER_ERROR)
     *  \sa upgradeAssetPackage  uninstallAssetPackage
     */
    public int installAssetPackage(String assetPackageFilePath, String licenseFilePath, int type, boolean synchronous, StringBuilder assetPackageId)
    {
        return nativeInstallAssetPackage(m_internalObject, assetPackageFilePath, licenseFilePath, type, synchronous, assetPackageId);
    }

    /*!
     *  \brief 升级资源包
     *  \param assetPackageFilePath 待升级资源包的文件路径
     *  \param licenseFilePath 待升级资源包的授权文件路径
     *  \param type 待升级资源包的类型。注：待输入参数是以ASSET_PACKAGE_TYPE打头的静态int属性值。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
     *  \param synchronous 是否同步升级。值为true则升级过程会阻塞当前线程直到升级成功或者失败，false则升级过程的结果通过delegate异步通知（但也在当前线程通知）。
     *  \param assetPackageId 输出参数，返回该资源包ID
     *  \return 返回表示资源包错误类型的整形值。
     *
     *  注意：返回值是ASSET_PACKAGE_MANAGER_ERROR打头的静态int属性值。若返回ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR: 表示升级成功（只有同步升级才可能返回这个)，任何其它返回值都表示升级错误。请参见[资源包管理错误类型](@ref ASSET_PACKAGE_MANAGER_ERROR)
     *  \sa installAssetPackage  uninstallAssetPackage
     */
    public int upgradeAssetPackage(String assetPackageFilePath, String licenseFilePath, int type, boolean synchronous, StringBuilder assetPackageId)
    {
        return nativeUpgradeAssetPackage(m_internalObject, assetPackageFilePath, licenseFilePath, type, synchronous, assetPackageId);
    }

    /*!
     *  \brief 卸载资源包
     *  \param assetPackageId 待卸载资源包的ID
     *  \param type 待卸载资源包的类型。注：待输入参数是以ASSET_PACKAGE_TYPE打头的静态int属性值。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
     *  \return 返回表示资源包错误类型的整形值。
     *
     *  注意：返回值是ASSET_PACKAGE_MANAGER_ERROR打头的静态int属性值。若返回ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR: 表示卸载成功，其它则卸载失败。请参见[资源包管理错误类型](@ref ASSET_PACKAGE_MANAGER_ERROR)
     *  \sa installAssetPackage  upgradeAssetPackage
     */
    public int uninstallAssetPackage(String assetPackageId, int type)
    {
        return nativeUninstallAssetPackage(m_internalObject, assetPackageId, type);
    }

    /*!
     *  \brief 获取资源包的当前状态
     *  \param assetPackageId 待查询状态资源包的ID
     *  \param type 待查询状态资源包的类型。注：待输入参数是以ASSET_PACKAGE_TYPE打头的静态int属性值。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
     *  \return 返回资源包的当前状态值。
     *
     *  注意：返回值是ASSET_PACKAGE_STATUS打头的静态int属性值。返回值是0，即返回ASSET_PACKAGE_STATUS_NOTINSTALLED，表示尚未安装。请参见[资源包状态](@ref ASSET_PACKAGE_STATUS)
     */
    public int getAssetPackageStatus(String assetPackageId, int type)
    {
        return nativeGetAssetPackageStatus(m_internalObject, assetPackageId, type);
    }

    /*!
     *  \brief 获取资源包的版本
     *  \param assetPackageId 资源包ID
     *  \param type 资源包类型。注：待输入参数是以ASSET_PACKAGE_TYPE打头的静态int属性值。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
     *  \return 返回获得的资源包版本
     */
    public int getAssetPackageVersion(String assetPackageId, int type)
    {
        return nativeGetAssetPackageVersion(m_internalObject, assetPackageId, type);
    }

    /*!
     *  \brief 获取某个类型的资源包列表
     *  \param type 资源包类型。注：待输入参数是以ASSET_PACKAGE_TYPE打头的静态int属性值。请参见[资源包类型](@ref ASSET_PACKAGE_TYPE)
     *  \return 返回表示某个类型下资源包的的列表。列表里的每个元素都是一个资源包ID
     */
    public List<String> getAssetPackageListOfType(int type)
    {
        return nativeGetAssetPackageListOfType(m_internalObject, type);
    }

    private native void nativeSetCallbackInterface(AssetPackageManagerCallback callbackInterface);
    private native void nativeSetInternalCallbackObject();
    private native String nativeGetAssetPackageIdFromAssetPackageFilePath(long internalObject, String assetPackageFilePath);
    private native int nativeInstallAssetPackage(long internalObject, String assetPackageFilePath, String licenseFilePath, int type, boolean synchronous, StringBuilder assetPackageId);
    private native int nativeUpgradeAssetPackage(long internalObject, String assetPackageFilePath, String licenseFilePath, int type, boolean synchronous, StringBuilder assetPackageId);
    private native int nativeUninstallAssetPackage(long internalObject, String assetPackageId, int type);
    private native int nativeGetAssetPackageStatus(long internalObject, String assetPackageId, int type);
    private native int nativeGetAssetPackageVersion(long internalObject, String assetPackageId, int type);
    private native List<String> nativeGetAssetPackageListOfType(long internalObject, int type);
}

