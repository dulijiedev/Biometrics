package com.dolj.biometrics.utils

/**
 * @author: dlj
 * @date: 2020/5/13
 */


const val FONT_SECONDARY_COLOR = "FONT_SECONDARY_COLOR"

const val COLOR_BG_PRIMARY = "COLOR_BG_PRIMARY"

const val COLOR_DEFAULT_PATTERN = "COLOR_DEFAULT_PATTERN"

const val COLOR_SELECTED_PATTERN = "COLOR_SELECTED_PATTERN"

const val COLOR_ERROR_PATTERN = "COLOR_ERROR_PATTERN"

const val GESTURE_PASSWORD = "GESTURE_PASSWORD"

const val FORGET_TEXT = "FORGET_TEXT"

const val THEME_COLOR = "THEME_COLOR"


/***
 * SharePreference
 *   var colorFontSecondary: Int? = null
var colorBgPrimary: Int? = null
var colorDefaultPattern: Int? = null
var colorSelectedPattern: Int? = null
var colorErrorPattern: Int? = null
 */


var fontSecondary: Int by DelegatesExt.preference(UIUtils.getContext(), FONT_SECONDARY_COLOR, 0)

var bgPrimary: Int by DelegatesExt.preference(UIUtils.getContext(), COLOR_BG_PRIMARY, 0)

var defaultPattern: Int by DelegatesExt.preference(UIUtils.getContext(), COLOR_DEFAULT_PATTERN, 0)

var selectedPattern: Int by DelegatesExt.preference(UIUtils.getContext(), COLOR_SELECTED_PATTERN, 0)

var errorPattern: Int by DelegatesExt.preference(UIUtils.getContext(), COLOR_ERROR_PATTERN, 0)

var themeColors: Int by DelegatesExt.preference(UIUtils.getContext(), THEME_COLOR, 0)

var gesturePwd: String by DelegatesExt.preference(UIUtils.getContext(), GESTURE_PASSWORD, "")

var forgetText: String by DelegatesExt.preference(UIUtils.getContext(), FORGET_TEXT, "")

const val DEFAULT_KEY_NAME = "default_key"

const val FINGER_TITLE = "finger_title"
const val FINGER_SUB_TITLE = "finger_subtitle"
const val FINGER_DESC = "finger_desc"
const val FINGER_NET_TEXT = "finger_negative_text"

var fingerTitle: String by DelegatesExt.preference(UIUtils.getContext(), FINGER_TITLE, "")
var fingerSubTitle: String by DelegatesExt.preference(UIUtils.getContext(), FINGER_SUB_TITLE, "")
var fingerDesc: String by DelegatesExt.preference(UIUtils.getContext(), FINGER_DESC, "")
var fingerNegativeText: String by DelegatesExt.preference(UIUtils.getContext(), FINGER_NET_TEXT, "")
