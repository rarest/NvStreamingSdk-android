//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    Mar 8. 2017
//   Author:        NewAuto video team
//================================================================================
package com.meicam.sdk;

import java.util.*;

/*!
    \brief 特效参数描述

    在视频拍摄和编辑时会设置各种不同参数类型的特效，特效参数描述就是专门用来获取各种特效参数值的，以便查看和了解。
*/
public class NvsFxDescription
{
    /*!
        \brief 特效参数信息类，主要是特效参数的详细信息
    */
    public static class ParamInfoObject
    {
        /*! \anchor PARAM_TYPE_INT */
        /*! @name 特效参数类型 */
        //!@{
        public static final String PARAM_TYPE_INT = "INT";
        public static final String PARAM_TYPE_FLOAT = "FLOAT";
        public static final String PARAM_TYPE_BOOL = "BOOL";
        public static final String PARAM_TYPE_MENU = "MENU";
        public static final String PARAM_TYPE_STRING = "STRING";
        public static final String PARAM_TYPE_COLOR = "COLOR";
        public static final String PARAM_TYPE_POSITION2D = "POSITION2D";
        public static final String PARAM_TYPE_POSITION3D = "POSITION3D";

        //!@}

        /*! \anchor PARAM_STRING_TYPE_INVALID */
        /*! @name 字符串参数类型 */
        //!@{
        public static final int PARAM_STRING_TYPE_INVALID = -1;   //!< 无效
        public static final int PARAM_STRING_TYPE_SINGLELINE = 0; //!< 单行
        public static final int PARAM_STRING_TYPE_MULTILINE = 1;  //!< 多行
        public static final int PARAM_STRING_TYPE_FILEPATH = 2;   //!< 文件路径
        public static final int PARAM_STRING_TYPE_DIRECTORYPATH = 3; //!< 目录路径
        public static final int PARAM_STRING_TYPE_LABEL = 4;      //!< 标签

        //!@}
        /*! \anchor PARAM_NAME */
        public static final String PARAM_NAME = "paramName";
        /*! \anchor PARAM_TYPE */
        public static final String PARAM_TYPE = "paramType";

        /*! \anchor PARAM_INT_DEF_VAL */
        /*! @name 特效参数整数类型的默认值和取值范围 */
        //!@{
        public static final String PARAM_INT_DEF_VAL = "intDefVal";
        public static final String PARAM_INT_MIN_VAL = "intMinVal";
        public static final String PARAM_INT_MAX_VAL = "intMaxVal";

        //!@}

        /*! \anchor PARAM_FLOAT_DEF_VAL */
        /*! @name 特效参数浮点数类型的默认值和取值范围 */
        //!@{
        public static final String PARAM_FLOAT_DEF_VAL = "floatDefVal";
        public static final String PARAM_FLOAT_MIN_VAL = "floatMinVal";
        public static final String PARAM_FLOAT_MAX_VAL = "floatMaxVal";

        //!@}

    /*! \anchor PARAM_BOOL_DEF_VAL */
        /*! @name 特效参数布尔值类型的默认值 */
        //!@{
        public static final String PARAM_BOOL_DEF_VAL = "boolDefVal";
        //!@}

        /*! \anchor PARAM_COLOR_DEF_R */
        /*! @name 特效参数颜色值类型的默认值 */
        //!@{
        public static final String PARAM_COLOR_DEF_R = "colorDefR";
        public static final String PARAM_COLOR_DEF_G = "colorDefG";
        public static final String PARAM_COLOR_DEF_B = "colorDefB";
        public static final String PARAM_COLOR_DEF_A = "colorDefA";

        //!@}

        /*! \anchor PARAM_POSITION2D_DEF_X */
        /*! @name 特效参数二维坐标的默认值 */
        //!@{
        public static final String PARAM_POSITION2D_DEF_X = "position2DDefX";
        public static final String PARAM_POSITION2D_DEF_Y = "position2DDefY";

        //!@}

        /*! \anchor PARAM_POSITION3D_DEF_X */
        /*! @name 特效参数三维坐标的默认值 */
        //!@{
        public static final String PARAM_POSITION3D_DEF_X = "position3DDefX";
        public static final String PARAM_POSITION3D_DEF_Y = "position3DDefY";
        public static final String PARAM_POSITION3D_DEF_Z = "position3DDefZ";

        //!@}
        /*! \anchor PARAM_STRING_TYPE */
        public static final String PARAM_STRING_TYPE = "stringType";
        /*! \anchor PARAM_STRING_DEF */
        public static final String PARAM_STRING_DEF = "stringDef";
        /*! \anchor PARAM_MENU_DEF_VAL */
        public static final String PARAM_MENU_DEF_VAL = "menuDefVal";

        /*! \anchor PARAM_MENU_ARRAY */
        public static final String PARAM_MENU_ARRAY = "menuArray";

        private Map<String, Object> m_mapParams;

        ParamInfoObject()
        {
            m_mapParams = new HashMap<String, Object>();
        }
    /*!
       \brief 获取特效参数整数类型的默认值或取值范围
       \param name  要获取的特效参数整数类型的取值范围定义

       如：设置[PARAM_INT_DEF_VAL](@ref PARAM_INT_DEF_VAL)则表示获取特效参数整数类型的默认值；
       [PARAM_INT_MIN_VAL] (@ref PARAM_INT_DEF_VAL)表示获取特效参数整数类型的最小值；
       [PARAM_INT_MAX_VAL] (@ref PARAM_INT_DEF_VAL)表示获取特效参数整数类型的最大值。
       在使用方法setIntVal设置获得特效参数整数值时，设置的整数值需要在
       获取的最小值和最大值之间。

       \return 返回获得特效参数整数类型的默认值或取值范围值
       \sa com.meicam.sdk.NvsFx.setIntVal
    */
        public final int getInteger(String name) {
            return ((Integer)m_mapParams.get(name)).intValue();
        }
    /*!
       \brief 获取特效参数浮点数类型的默认值或取值范围
       \param name  要获取的特效参数浮点数类型的默认值或取值范围定义

       如：设置[PARAM_FLOAT_DEF_VAL](@ref PARAM_FLOAT_DEF_VAL)则表示获取特效参数浮点数类型的默认值；
       [PARAM_FLOAT_MIN_VAL] (@ref PARAM_FLOAT_DEF_VAL)表示获取特效参数浮点数类型的最小值
       [PARAM_FLOAT_MAX_VAL] (@ref PARAM_FLOAT_DEF_VAL)表示获取特效参数浮点数类型的最大值。
       在使用方法setFloatVal设置设置特效参数浮点值时，设置的浮点数需要在
       获取的最小值和最大值之间。

       获取特效参数颜色值、二维坐标、三维坐标的默认值也是使用此方法获取。
       例：设置[PARAM_COLOR_DEF_R] (@ref PARAM_COLOR_DEF_R)表示获取特效参数颜色值的红颜色值
           设置[PARAM_POSITION2D_DEF_X] (@ref PARAM_POSITION2D_DEF_X)表示获取特效参数二维坐标的横坐标
           设置[PARAM_POSITION3D_DEF_X] (@ref PARAM_POSITION3D_DEF_X)表示获取特效参数三维坐标的横坐标


       \return 返回获得特效参数浮点数类型的默认值或取值范围值
       \sa com.meicam.sdk.NvsFx.setFloatVal
    */
        public final double getFloat(String name) {
            return ((Double)m_mapParams.get(name)).doubleValue();
        }
    /*!
       \brief 获取字符串参数以及特效参数的默认值或类型
       \param name  要获取的字符串参数默认值或类型的定义

       设置[PARAM_STRING_TYPE](@ref PARAM_STRING_TYPE)为获取字符串参数的类型（类型包括[PARAM_STRING_TYPE_*] (@ref PARAM_STRING_TYPE_INVALID)）
       [PARAM_STRING_DEF] (@ref PARAM_STRING_DEF)为获取字符串参数的默认值
       [PARAM_MENU_DEF_VAL] (@ref PARAM_MENU_DEF_VAL)为获取菜单参数的默认值
       [PARAM_TYPE] (@ref PARAM_TYPE)为获取特效参数的类型(类型包括[PARAM_TYPE_*] (@ref PARAM_TYPE_INT))
       [PARAM_NAME] (@ref PARAM_NAME)为获取特效参数的名字
       \return 返回获得的字符串参数以及特效参数的默认值或类型
       \sa com.meicam.sdk.NvsFx.setStringVal
       \sa com.meicam.sdk.NvsFx.setColorVal
       \sa com.meicam.sdk.NvsFx.setPosition2DVal
    */
        public final String getString(String name) {
            return (String)m_mapParams.get(name);
        }
    /*!
       \brief 获取菜单参数值的对象
       \param name  要获取的菜单参数对象的定义[PARAM_MENU_ARRAY](@ref PARAM_MENU_ARRAY)
       \return 返回获取的菜单参数对象

    */
        public final Object getObject(String name) {
            return m_mapParams.get(name);
        }
    /*!
       \brief 获取特效参数布尔值类型的默认值
       \param name  要获取的特效参数布尔值默认值的定义[PARAM_BOOL_DEF_VAL](@ref PARAM_BOOL_DEF_VAL)
       \return 返回获得特效参数布尔值类型的默认值
       \sa com.meicam.sdk.NvsFx.setBooleanVal
    */
        public final boolean getBoolean(String name) {
            return ((Integer)m_mapParams.get(name)).intValue() == 0 ? false : true;
        }

        final void setInteger(String name, int value) {
            m_mapParams.put(name, Integer.valueOf(value));
        }

        final void setFloat(String name, double value) {
            m_mapParams.put(name, Double.valueOf(value));
        }

        final void setString(String name, String value) {
            m_mapParams.put(name, value);
        }

        final void setObject(String name, Object value) {
            m_mapParams.put(name, value);
        }
    }

    long m_fxDescription = 0;

    void setFxDescription(long fxDescription)
    {
        m_fxDescription = fxDescription;
    }
/*!
     \brief 获取特效的名字
     \return 返回获取的特效的名字
*/
    public String getName()
    {
        return nativeGetName(m_fxDescription);
    }
/*!
     \brief 获取所有的特效的信息
     \return 返回获取的包含所有特效信息对象的List集合
*/
    public List<ParamInfoObject> getAllParamsInfo()
    {
        return nativeGetAllParamsInfo(m_fxDescription);
    }

    private native String nativeGetName(long fxDescription);
    private native List<ParamInfoObject> nativeGetAllParamsInfo(long fxDescription);
}
