package com.sentaro.yanlang.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val RED_FLAG_TAG = "RedFlagConfirmation"
private const val RED_FLAG_ABOUT_URL = "https://search3958.github.io/"
private const val RED_FLAG_POLICIES_URL = "https://search3958.github.io/policies/"
private const val RED_FLAG_CONTACT_URL = "https://docs.google.com/forms/d/e/1FAIpQLSegCKF2UdLdEA7cQ6y3PS3vlZ8fT29KnEyo26RDl15ocIM1Ig/viewform"
private const val RED_FLAG_VERSION = "3.0"
private const val RED_FLAG_SETTINGS_NAME = "red_flag_confirmation_settings"
private const val RED_FLAG_LANGUAGE_KEY = "display_language"
private const val RED_FLAG_INSTALLER_GOOGLE_PLAY = "com.android.vending"
private const val RED_FLAG_INSTALLER_HUAWEI = "com.huawei.appmarket"
private const val RED_FLAG_INSTALLER_GALAXY = "com.sec.android.app.samsungapps"

private val RED_FLAG_HEADER_COLOR = Color(0xFF212121)
private val RED_FLAG_BACKGROUND_COLOR = Color(0xFFFAFAFA)
private val RED_FLAG_CARD_COLOR = Color.White
private val RED_FLAG_TEXT_COLOR = Color(0xFF212121)
private val RED_FLAG_SECONDARY_TEXT_COLOR = Color(0xFF757575)
private val RED_FLAG_GRANTED_COLOR = Color(0xFF2E7D32)
private val RED_FLAG_SMALL_CORNER_SHAPE = RoundedCornerShape(2.dp)
private val RED_FLAG_M2_PURPLE = Color(0xFF6200EE)
private val RED_FLAG_M2_MINT = Color(0xFF009688)
private val RED_FLAG_DIALOG_BUTTON_SHAPE = RoundedCornerShape(2.dp)
private val RED_FLAG_FAB_SHAPE = RoundedCornerShape(50)

private enum class RedFlagLanguage(
    val code: String,
    val nativeName: String
) {
    JA(code = "ja", nativeName = "日本語"),
    EN_GB(code = "en-gb", nativeName = "English (UK)"),
    ZH_CN(code = "zh-cn", nativeName = "简体中文"),
    ZH_TW(code = "zh-tw", nativeName = "繁體中文"),
    KO_STANDARD(code = "ko-standard", nativeName = "한국어"),
    EN_US(code = "en-us", nativeName = "English (US)"),
    HI(code = "hi", nativeName = "हिन्दी"),
    ES(code = "es", nativeName = "Español"),
    AR(code = "ar", nativeName = "العربية"),
    FR(code = "fr", nativeName = "Français"),
    BN(code = "bn", nativeName = "বাংলা"),
    PT(code = "pt", nativeName = "Português"),
    ID(code = "id", nativeName = "Bahasa Indonesia"),
    UR(code = "ur", nativeName = "اردو"),
    KO_NORTH(code = "ko-north", nativeName = "조선문화어");

    companion object {
        fun fromCode(code: String?): RedFlagLanguage {
            return entries.firstOrNull { it.code == code } ?: JA
        }
    }
}

private data class RedFlagStrings(
    val title: String,
    val greeting: String,
    val verifiedLine: String,
    val unverifiedLine: String,
    val runningWithPackage: String,
    val about: String,
    val policies: String,
    val contact: String,
    val details: String,
    val permissions: String,
    val noPermissions: String,
    val detailsDialogTitle: String,
    val close: String,
    val languageDialogTitle: String,
    val browserDialogTitle: String,
    val browserDialogText: String,
    val continueText: String,
    val cancel: String,
    val warningDetails: String,
    val warningTitle: String,
    val warningText: String,
    val systemLanguage: String,
    val environment: String,
    val versionCode: String,
    val displayTime: String,
    val packageName: String,
    val installer: String,
    val safetySystem: String,
    val verified: String,
    val unverified: String,
    val smartphone: String,
    val tablet: String,
    val unknown: String,
    val installerGooglePlay: String,
    val installerHuawei: String,
    val installerGalaxy: String,
    val installerUnknown: String,
    val permitted: String
)

private data class RedFlagPermissionInfo(
    val name: String,
    val label: String,
    val granted: Boolean
)

private data class RedFlagInstallerInfo(
    val packageName: String?,
    val displayName: String,
    val isVerifiedStore: Boolean
)

private data class RedFlagAppDetails(
    val language: String,
    val environment: String,
    val versionCode: String,
    val packageName: String,
    val installer: String,
    val safetySystemVersion: String
)

private fun redFlagStrings(language: RedFlagLanguage): RedFlagStrings {
    return when (language) {
        RedFlagLanguage.JA -> RedFlagStrings(
            title = "確認体系",
            greeting = "こんにちは",
            verifiedLine = "アプリは正規版である可能性が高いです",
            unverifiedLine = "アプリの完全性を保証できません",
            runningWithPackage = "%s で実行中",
            about = "私について",
            policies = "利用規約と個人情報処理政策",
            contact = "問い合わせ",
            details = "詳細を表示",
            permissions = "権限",
            noPermissions = "このアプリで使用されている権限はありません。",
            detailsDialogTitle = "詳細",
            close = "閉じる",
            languageDialogTitle = "言語",
            browserDialogTitle = "ブラウザで開きます",
            browserDialogText = "外部ブラウザでページを開きます。\n続行しますか？",
            continueText = "続行",
            cancel = "キャンセル",
            warningDetails = "詳細",
            warningTitle = "アプリの完全性を確認できません",
            warningText = "アプリが正規ストアからインストールされていない可能性があります。",
            systemLanguage = "操作体系設定言語",
            environment = "利用環境",
            versionCode = "アプリ自体のバージョンコード",
            displayTime = "表示時刻",
            packageName = "アプリパッケージ名",
            installer = "インストーラ",
            safetySystem = "安全確認体系",
            verified = "検証済み",
            unverified = "未検証",
            smartphone = "スマートフォン",
            tablet = "タブレット",
            unknown = "不明",
            installerGooglePlay = "Google Play ストア",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "不明なインストーラ",
            permitted = "許可されています"
        )
        RedFlagLanguage.EN_GB, RedFlagLanguage.EN_US -> RedFlagStrings(
            title = "Verification",
            greeting = "Hello",
            verifiedLine = "The app has been verified",
            unverifiedLine = "The app's integrity cannot be guaranteed",
            runningWithPackage = "Running with %s",
            about = "About me",
            policies = "Terms of Use & Privacy Policy",
            contact = "Contact",
            details = "Show details",
            permissions = "Permissions",
            noPermissions = "This app does not use any permissions.",
            detailsDialogTitle = "Details",
            close = "Close",
            languageDialogTitle = "Language",
            browserDialogTitle = "Open in browser",
            browserDialogText = "This page will be opened in an external browser.\nContinue?",
            continueText = "Continue",
            cancel = "Cancel",
            warningDetails = "Details",
            warningTitle = "App integrity could not be verified",
            warningText = "The app may not have been installed from an official app store.",
            systemLanguage = "System language",
            environment = "Environment",
            versionCode = "App version code",
            displayTime = "Display time",
            packageName = "App package name",
            installer = "Installer",
            safetySystem = "Safety verification system",
            verified = "Verified",
            unverified = "Unverified",
            smartphone = "Smartphone",
            tablet = "Tablet",
            unknown = "Unknown",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "Unknown installer",
            permitted = "Permission granted"
        )
        RedFlagLanguage.ZH_CN -> RedFlagStrings(
            title = "确认体系",
            greeting = "您好",
            verifiedLine = "应用已通过验证",
            unverifiedLine = "无法保证应用完整性",
            runningWithPackage = "正在以 %s 运行",
            about = "关于我",
            policies = "使用条款和隐私政策",
            contact = "联系我们",
            details = "显示详细信息",
            permissions = "权限",
            noPermissions = "此应用未使用任何权限。",
            detailsDialogTitle = "详细信息",
            close = "关闭",
            languageDialogTitle = "语言",
            browserDialogTitle = "在浏览器中打开",
            browserDialogText = "此页面将在外部浏览器中打开。\n是否继续？",
            continueText = "继续",
            cancel = "取消",
            warningDetails = "详细信息",
            warningTitle = "无法验证应用完整性",
            warningText = "此应用可能不是从官方应用商店安装的。",
            systemLanguage = "系统语言",
            environment = "运行环境",
            versionCode = "应用版本代码",
            displayTime = "显示时间",
            packageName = "应用包名",
            installer = "安装程序",
            safetySystem = "安全确认体系",
            verified = "已验证",
            unverified = "未验证",
            smartphone = "智能手机",
            tablet = "平板电脑",
            unknown = "未知",
            installerGooglePlay = "Google Play 商店",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "未知安装程序",
            permitted = "已允许"
        )
        RedFlagLanguage.ZH_TW -> RedFlagStrings(
            title = "確認體系",
            greeting = "您好",
            verifiedLine = "應用程式已通過驗證",
            unverifiedLine = "無法保證應用程式完整性",
            runningWithPackage = "正在以 %s 執行",
            about = "關於我",
            policies = "使用條款與隱私權政策",
            contact = "聯絡我們",
            details = "顯示詳細資訊",
            permissions = "權限",
            noPermissions = "此應用程式未使用任何權限。",
            detailsDialogTitle = "詳細資訊",
            close = "關閉",
            languageDialogTitle = "語言",
            browserDialogTitle = "在瀏覽器中開啟",
            browserDialogText = "此頁面將在外部瀏覽器中開啟。\n是否繼續？",
            continueText = "繼續",
            cancel = "取消",
            warningDetails = "詳細資訊",
            warningTitle = "無法驗證應用程式完整性",
            warningText = "此應用程式可能不是從官方應用程式商店安裝。",
            systemLanguage = "系統語言",
            environment = "執行環境",
            versionCode = "應用程式版本代碼",
            displayTime = "顯示時間",
            packageName = "應用程式套件名稱",
            installer = "安裝程式",
            safetySystem = "安全確認體系",
            verified = "已驗證",
            unverified = "未驗證",
            smartphone = "智慧型手機",
            tablet = "平板電腦",
            unknown = "未知",
            installerGooglePlay = "Google Play 商店",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "未知安裝程式",
            permitted = "已允許"
        )
        RedFlagLanguage.HI -> RedFlagStrings(
            title = "सत्यापन प्रणाली",
            greeting = "नमस्ते",
            verifiedLine = "ऐप सत्यापित है",
            unverifiedLine = "ऐप की पूर्णता की गारंटी नहीं दी जा सकती",
            runningWithPackage = "%s के साथ चल रहा है",
            about = "मेरे बारे में",
            policies = "उपयोग की शर्तें और गोपनीयता नीति",
            contact = "संपर्क",
            details = "विवरण दिखाएं",
            permissions = "अनुमतियाँ",
            noPermissions = "यह ऐप किसी अनुमति का उपयोग नहीं करता।",
            detailsDialogTitle = "विवरण",
            close = "बंद करें",
            languageDialogTitle = "भाषा",
            browserDialogTitle = "ब्राउज़र में खोलें",
            browserDialogText = "यह पृष्ठ बाहरी ब्राउज़र में खुलेगा।\nक्या जारी रखना है؟",
            continueText = "जारी रखें",
            cancel = "रद्द करें",
            warningDetails = "विवरण",
            warningTitle = "ऐप की पूर्णता सत्यापित नहीं की जा सकी",
            warningText = "हो सकता है ऐप किसी आधिकारिक ऐप स्टोर से इंस्टॉल न किया गया हो।",
            systemLanguage = "सिस्टम भाषा",
            environment = "पर्यावरण",
            versionCode = "ऐप संस्करण कोड",
            displayTime = "प्रदर्शन समय",
            packageName = "ऐप पैकेज नाम",
            installer = "इंस्टॉलर",
            safetySystem = "सुरक्षा सत्यापन प्रणाली",
            verified = "सत्यापित",
            unverified = "सत्यापित नहीं",
            smartphone = "स्मार्टफोन",
            tablet = "टैबलेट",
            unknown = "अज्ञात",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "अज्ञात इंस्टॉलर",
            permitted = "अनुमति दी गई"
        )
        RedFlagLanguage.ES -> RedFlagStrings(
            title = "Verificación",
            greeting = "Hola",
            verifiedLine = "La aplicación ha sido verificada",
            unverifiedLine = "No se puede garantizar la integridad de la aplicación",
            runningWithPackage = "Ejecutándose con %s",
            about = "Sobre mí",
            policies = "Condiciones de uso y política de privacidad",
            contact = "Contacto",
            details = "Mostrar detalles",
            permissions = "Permisos",
            noPermissions = "Esta aplicación no utiliza ningún permiso.",
            detailsDialogTitle = "Detalles",
            close = "Cerrar",
            languageDialogTitle = "Idioma",
            browserDialogTitle = "Abrir en el navegador",
            browserDialogText = "Esta página se abrirá en un navegador externo.\n¿Continuar?",
            continueText = "Continuar",
            cancel = "Cancelar",
            warningDetails = "Detalles",
            warningTitle = "No se pudo verificar la integridad de la aplicación",
            warningText = "Es posible que la aplicación no se haya instalado desde una tienda oficial.",
            systemLanguage = "Idioma del sistema",
            environment = "Entorno",
            versionCode = "Código de versión de la aplicación",
            displayTime = "Hora de visualización",
            packageName = "Nombre del paquete",
            installer = "Instalador",
            safetySystem = "Sistema de verificación de seguridad",
            verified = "Verificada",
            unverified = "No verificada",
            smartphone = "Teléfono inteligente",
            tablet = "Tableta",
            unknown = "Desconocido",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "Instalador desconocido",
            permitted = "Permiso concedido"
        )
        RedFlagLanguage.AR -> RedFlagStrings(
            title = "نظام التحقق",
            greeting = "مرحبًا",
            verifiedLine = "تم التحقق من التطبيق",
            unverifiedLine = "لا يمكن ضمان سلامة التطبيق",
            runningWithPackage = "يعمل باستخدام %s",
            about = "من أنا",
            policies = "شروط الاستخدام وسياسة الخصوصية",
            contact = "اتصل بنا",
            details = "عرض التفاصيل",
            permissions = "الأذونات",
            noPermissions = "لا يستخدم هذا التطبيق أي أذونات.",
            detailsDialogTitle = "التفاصيل",
            close = "إغلاق",
            languageDialogTitle = "اللغة",
            browserDialogTitle = "فتح في المتصفح",
            browserDialogText = "سيتم فتح هذه الصفحة في متصفح خارجي.\nهل تريد المتابعة؟",
            continueText = "متابعة",
            cancel = "إلغاء",
            warningDetails = "التفاصيل",
            warningTitle = "تعذر التحقق من سلامة التطبيق",
            warningText = "قد لا يكون التطبيق قد تم تثبيته من متجر تطبيقات رسمي.",
            systemLanguage = "لغة النظام",
            environment = "البيئة",
            versionCode = "رمز إصدار التطبيق",
            displayTime = "وقت العرض",
            packageName = "اسم حزمة التطبيق",
            installer = "المثبّت",
            safetySystem = "نظام التحقق الأمني",
            verified = "تم التحقق",
            unverified = "غير متحقق",
            smartphone = "هاتف ذكي",
            tablet = "جهاز لوحي",
            unknown = "غير معروف",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "مثبّت غير معروف",
            permitted = "مسموح"
        )
        RedFlagLanguage.FR -> RedFlagStrings(
            title = "Vérification",
            greeting = "Bonjour",
            verifiedLine = "L'application a été vérifiée",
            unverifiedLine = "L'intégrité de l'application ne peut pas être garantie",
            runningWithPackage = "Exécution avec %s",
            about = "À propos de moi",
            policies = "Conditions d'utilisation et politique de confidentialité",
            contact = "Contact",
            details = "Afficher les détails",
            permissions = "Autorisations",
            noPermissions = "Cette application n'utilise aucune autorisation.",
            detailsDialogTitle = "Détails",
            close = "Fermer",
            languageDialogTitle = "Langue",
            browserDialogTitle = "Ouvrir dans le navigateur",
            browserDialogText = "Cette page sera ouverte dans un navigateur externe.\nContinuer ?",
            continueText = "Continuer",
            cancel = "Annuler",
            warningDetails = "Détails",
            warningTitle = "L'intégrité de l'application n'a pas pu être vérifiée",
            warningText = "L'application peut ne pas avoir été installée depuis une boutique officielle.",
            systemLanguage = "Langue du système",
            environment = "Environnement",
            versionCode = "Code de version de l'application",
            displayTime = "Heure d'affichage",
            packageName = "Nom du paquet",
            installer = "Installateur",
            safetySystem = "Système de vérification de sécurité",
            verified = "Vérifiée",
            unverified = "Non vérifiée",
            smartphone = "Smartphone",
            tablet = "Tablette",
            unknown = "Inconnu",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "Installateur inconnu",
            permitted = "Autorisation accordée"
        )
        RedFlagLanguage.BN -> RedFlagStrings(
            title = "যাচাইকরণ ব্যবস্থা",
            greeting = "হ্যালো",
            verifiedLine = "অ্যাপটি যাচাই করা হয়েছে",
            unverifiedLine = "অ্যাপের অখণ্ডতার নিশ্চয়তা দেওয়া যায় না",
            runningWithPackage = "%s দিয়ে চলছে",
            about = "আমার সম্পর্কে",
            policies = "ব্যবহারের শর্তাবলি ও গোপনীয়তা নীতি",
            contact = "যোগাযোগ",
            details = "বিস্তারিত দেখুন",
            permissions = "অনুমতি",
            noPermissions = "এই অ্যাপ কোনো অনুমতি ব্যবহার করে না।",
            detailsDialogTitle = "বিস্তারিত",
            close = "বন্ধ করুন",
            languageDialogTitle = "ভাষা",
            browserDialogTitle = "ব্রাউজারে খুলুন",
            browserDialogText = "এই পৃষ্ঠাটি একটি বাহ্যিক ব্রাউজারে খোলা হবে।\nচালিয়ে যাবেন?",
            continueText = "চালিয়ে যান",
            cancel = "বাতিল",
            warningDetails = "বিস্তারিত",
            warningTitle = "অ্যাপের অখণ্ডতা যাচাই করা যায়নি",
            warningText = "অ্যাপটি কোনো অফিসিয়াল অ্যাপ স্টোর থেকে ইনস্টল করা নাও হয়ে থাকতে পারে।",
            systemLanguage = "সিস্টেম ভাষা",
            environment = "পরিবেশ",
            versionCode = "অ্যাপ সংস্করণ কোড",
            displayTime = "প্রদর্শনের সময়",
            packageName = "অ্যাপ প্যাকেজ নাম",
            installer = "ইনস্টলার",
            safetySystem = "নিরাপত্তা যাচাইকরণ ব্যবস্থা",
            verified = "যাচাইকৃত",
            unverified = "যাচাইকৃত নয়",
            smartphone = "স্মার্টফোন",
            tablet = "ট্যাবলেট",
            unknown = "অজানা",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "অজানা ইনস্টলার",
            permitted = "অনুমতি দেওয়া হয়েছে"
        )
        RedFlagLanguage.PT -> RedFlagStrings(
            title = "Verificação",
            greeting = "Olá",
            verifiedLine = "O aplicativo foi verificado",
            unverifiedLine = "Não é possível garantir a integridade do aplicativo",
            runningWithPackage = "Executando com %s",
            about = "Sobre mim",
            policies = "Termos de uso e política de privacidade",
            contact = "Contato",
            details = "Mostrar detalhes",
            permissions = "Permissões",
            noPermissions = "Este aplicativo não usa nenhuma permissão.",
            detailsDialogTitle = "Detalhes",
            close = "Fechar",
            languageDialogTitle = "Idioma",
            browserDialogTitle = "Abrir no navegador",
            browserDialogText = "Esta página será aberta em um navegador externo.\nContinuar?",
            continueText = "Continuar",
            cancel = "Cancelar",
            warningDetails = "Detalhes",
            warningTitle = "Não foi possível verificar a integridade do aplicativo",
            warningText = "O aplicativo pode não ter sido instalado de uma loja oficial.",
            systemLanguage = "Idioma do sistema",
            environment = "Ambiente",
            versionCode = "Código da versão do aplicativo",
            displayTime = "Hora de exibição",
            packageName = "Nome do pacote",
            installer = "Instalador",
            safetySystem = "Sistema de verificação de segurança",
            verified = "Verificado",
            unverified = "Não verificado",
            smartphone = "Smartphone",
            tablet = "Tablet",
            unknown = "Desconhecido",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "Instalador desconhecido",
            permitted = "Permissão concedida"
        )
        RedFlagLanguage.ID -> RedFlagStrings(
            title = "Verifikasi",
            greeting = "Halo",
            verifiedLine = "Aplikasi telah diverifikasi",
            unverifiedLine = "Integritas aplikasi tidak dapat dijamin",
            runningWithPackage = "Berjalan dengan %s",
            about = "Tentang saya",
            policies = "Ketentuan penggunaan dan kebijakan privasi",
            contact = "Kontak",
            details = "Tampilkan detail",
            permissions = "Izin",
            noPermissions = "Aplikasi ini tidak menggunakan izin apa pun.",
            detailsDialogTitle = "Detail",
            close = "Tutup",
            languageDialogTitle = "Bahasa",
            browserDialogTitle = "Buka di browser",
            browserDialogText = "Halaman ini akan dibuka di browser eksternal.\nLanjutkan?",
            continueText = "Lanjutkan",
            cancel = "Batal",
            warningDetails = "Detail",
            warningTitle = "Integritas aplikasi tidak dapat diverifikasi",
            warningText = "Aplikasi mungkin tidak diinstal dari toko aplikasi resmi.",
            systemLanguage = "Bahasa sistem",
            environment = "Lingkungan",
            versionCode = "Kode versi aplikasi",
            displayTime = "Waktu tampilan",
            packageName = "Nama paket aplikasi",
            installer = "Installer",
            safetySystem = "Sistem verifikasi keamanan",
            verified = "Terverifikasi",
            unverified = "Tidak terverifikasi",
            smartphone = "Ponsel pintar",
            tablet = "Tablet",
            unknown = "Tidak diketahui",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "Installer tidak dikenal",
            permitted = "Izin diberikan"
        )
        RedFlagLanguage.UR -> RedFlagStrings(
            title = "تصدیقی نظام",
            greeting = "السلام علیکم",
            verifiedLine = "ایپ کی تصدیق ہو گئی ہے",
            unverifiedLine = "ایپ کی سالمیت کی ضمانت نہیں دی جا سکتی",
            runningWithPackage = "%s کے ساتھ چل رہی ہے",
            about = "میرے بارے میں",
            policies = "استعمال کی شرائط اور رازداری کی پالیسی",
            contact = "رابطہ",
            details = "تفصیلات دکھائیں",
            permissions = "اجازتیں",
            noPermissions = "یہ ایپ کوئی اجازت استعمال نہیں کرتی۔",
            detailsDialogTitle = "تفصیلات",
            close = "بند کریں",
            languageDialogTitle = "زبان",
            browserDialogTitle = "براؤزر میں کھولیں",
            browserDialogText = "یہ صفحہ بیرونی براؤزر میں کھولا جائے گا۔\nکیا جاری رکھنا ہے؟",
            continueText = "جاری رکھیں",
            cancel = "منسوخ",
            warningDetails = "تفصیلات",
            warningTitle = "ایپ کی سالمیت کی تصدیق نہیں ہو سکی",
            warningText = "ممکن ہے کہ ایپ کسی سرکاری ایپ اسٹور سے انسٹال نہ کی گئی ہو۔",
            systemLanguage = "سسٹم کی زبان",
            environment = "ماحول",
            versionCode = "ایپ ورژن کوڈ",
            displayTime = "ظاہر ہونے کا وقت",
            packageName = "ایپ پیکیج نام",
            installer = "انسٹالر",
            safetySystem = "حفاظتی تصدیقی نظام",
            verified = "تصدیق شدہ",
            unverified = "غیر تصدیق شدہ",
            smartphone = "اسمارٹ فون",
            tablet = "ٹیبلیٹ",
            unknown = "نامعلوم",
            installerGooglePlay = "Google Play Store",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "نامعلوم انسٹالر",
            permitted = "اجازت دی گئی"
        )
        RedFlagLanguage.KO_STANDARD -> RedFlagStrings(
            title = "확인 체계",
            greeting = "안녕하세요",
            verifiedLine = "앱이 검증되었습니다",
            unverifiedLine = "앱의 무결성을 보장할 수 없습니다",
            runningWithPackage = "%s로 실행 중",
            about = "내 정보",
            policies = "이용 약관 및 개인정보 처리방침",
            contact = "문의",
            details = "상세 정보 보기",
            permissions = "권한",
            noPermissions = "이 앱에서 사용하는 권한이 없습니다.",
            detailsDialogTitle = "상세 정보",
            close = "닫기",
            languageDialogTitle = "언어",
            browserDialogTitle = "브라우저에서 열기",
            browserDialogText = "외부 브라우저에서 페이지를 엽니다.\n계속하시겠습니까?",
            continueText = "계속",
            cancel = "취소",
            warningDetails = "상세 정보",
            warningTitle = "앱의 무결성을 확인할 수 없습니다",
            warningText = "앱이 공식 앱 스토어에서 설치되지 않았을 가능성이 있습니다.",
            systemLanguage = "시스템 언어",
            environment = "사용 환경",
            versionCode = "앱 자체 버전 코드",
            displayTime = "표시 시각",
            packageName = "앱 패키지 이름",
            installer = "설치 프로그램",
            safetySystem = "안전 확인 체계",
            verified = "검증됨",
            unverified = "검증되지 않음",
            smartphone = "스마트폰",
            tablet = "태블릿",
            unknown = "알 수 없음",
            installerGooglePlay = "Google Play 스토어",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "알 수 없는 설치 프로그램",
            permitted = "허용됨"
        )
        RedFlagLanguage.KO_NORTH -> RedFlagStrings(
            title = "확인체계",
            greeting = "안녕하십니까",
            verifiedLine = "응용프로그람은 검증되였습니다",
            unverifiedLine = "응용프로그람의 완전성을 담보할수 없습니다",
            runningWithPackage = "%s으로 실행중",
            about = "나에 대하여",
            policies = "리용약관및 개인정보처리정책",
            contact = "문의",
            details = "상세정보 보기",
            permissions = "권한",
            noPermissions = "이 응용프로그람에서 사용하는 권한이 없습니다.",
            detailsDialogTitle = "상세정보",
            close = "닫기",
            languageDialogTitle = "언어",
            browserDialogTitle = "브라우저에서 열기",
            browserDialogText = "외부 브라우저에서 페지를 엽니다.\n계속하겠습니까?",
            continueText = "계속",
            cancel = "취소",
            warningDetails = "상세정보",
            warningTitle = "응용프로그람의 완전성을 확인할수 없습니다",
            warningText = "응용프로그람이 정규 상점에서 설치되지 않았을 가능성이 있습니다.",
            systemLanguage = "체계 설정 언어",
            environment = "리용환경",
            versionCode = "응용프로그람 자체 판번호 코드",
            displayTime = "표시시간",
            packageName = "응용프로그람 패키지명",
            installer = "설치자",
            safetySystem = "안전확인체계",
            verified = "검증됨",
            unverified = "검증되지 않음",
            smartphone = "손전화",
            tablet = "판형콤퓨터",
            unknown = "알수 없음",
            installerGooglePlay = "Google Play 스토어",
            installerHuawei = "HUAWEI AppGallery",
            installerGalaxy = "Galaxy Store",
            installerUnknown = "알수 없는 설치자",
            permitted = "허용됨"
        )
    }
}

class RedFlagConfirmationActivity : ComponentActivity() {

    private var redFlagPermissions by mutableStateOf<List<RedFlagPermissionInfo>>(emptyList())
    private var redFlagInstallerInfo by mutableStateOf(
        RedFlagInstallerInfo(packageName = null, displayName = "Unknown", isVerifiedStore = false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRedFlagPermissions()
        loadRedFlagInstallerInfo()
        setContent {
            RedFlagConfirmationScreen(
                onBack = { finish() },
                onOpenExternalUrl = { url ->
                    try {
                        val uri = Uri.parse(url)
                        if (uri.scheme == "http" || uri.scheme == "https") {
                            startActivity(Intent(Intent.ACTION_VIEW, uri))
                        }
                    } catch (exception: Exception) {
                        Log.e(RED_FLAG_TAG, "ブラウザ起動失敗: ${exception.message}", exception)
                    }
                },
                permissions = redFlagPermissions,
                installerInfo = redFlagInstallerInfo
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadRedFlagPermissions()
        loadRedFlagInstallerInfo()
    }

    private fun loadRedFlagPermissions() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val requestedPermissions = packageInfo.requestedPermissions ?: emptyArray()
            val permissionList = mutableListOf<RedFlagPermissionInfo>()
            for (permissionName in requestedPermissions) {
                if (permissionName.isBlank()) continue
                val label = getPermissionDisplayName(permissionName)
                val granted = isPermissionGranted(permissionName)
                permissionList.add(RedFlagPermissionInfo(name = permissionName, label = label, granted = granted))
            }
            redFlagPermissions = permissionList
        } catch (exception: Exception) {
            Log.e(RED_FLAG_TAG, "権限一覧の取得に失敗しました: ${exception.message}", exception)
            redFlagPermissions = emptyList()
        }
    }

    private fun loadRedFlagInstallerInfo() {
        try {
            val installerPackageName = getInstallerPackageName()
            val installerInfo = when (installerPackageName) {
                RED_FLAG_INSTALLER_GOOGLE_PLAY -> RedFlagInstallerInfo(installerPackageName, "Google Play Store", true)
                RED_FLAG_INSTALLER_HUAWEI -> RedFlagInstallerInfo(installerPackageName, "HUAWEI AppGallery", true)
                RED_FLAG_INSTALLER_GALAXY -> RedFlagInstallerInfo(installerPackageName, "Galaxy Store", true)
                null, "" -> RedFlagInstallerInfo(null, "Unknown installer", false)
                else -> RedFlagInstallerInfo(installerPackageName, "Unknown installer", false)
            }
            redFlagInstallerInfo = installerInfo
        } catch (exception: Exception) {
            Log.e(RED_FLAG_TAG, "インストーラ情報の取得に失敗しました: ${exception.message}", exception)
            redFlagInstallerInfo = RedFlagInstallerInfo(null, "Unknown installer", false)
        }
    }

    private fun getInstallerPackageName(): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                packageManager.getInstallSourceInfo(packageName).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                packageManager.getInstallerPackageName(packageName)
            }
        } catch (exception: Exception) {
            Log.e(RED_FLAG_TAG, "インストーラパッケージ名の取得に失敗しました: ${exception.message}", exception)
            null
        }
    }

    private fun getPermissionDisplayName(permissionName: String): String {
        return try {
            val permissionInfo = packageManager.getPermissionInfo(permissionName, 0)
            permissionInfo.loadLabel(packageManager)?.toString()?.trim() ?: permissionName.substringAfterLast('.')
        } catch (exception: PackageManager.NameNotFoundException) {
            permissionName.substringAfterLast('.')
        } catch (exception: Exception) {
            permissionName.substringAfterLast('.')
        }
    }

    private fun isPermissionGranted(permissionName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(permissionName) == PackageManager.PERMISSION_GRANTED
            } else {
                packageManager.checkPermission(permissionName, packageName) == PackageManager.PERMISSION_GRANTED
            }
        } catch (exception: Exception) {
            false
        }
    }

    private fun openPermissionSettings(permissionName: String) {
        try {
            val directPermissionIntent = Intent("android.intent.action.MANAGE_APP_PERMISSIONS").apply {
                putExtra(Intent.EXTRA_PACKAGE_NAME, packageName)
            }
            if (directPermissionIntent.resolveActivity(packageManager) != null) {
                startActivity(directPermissionIntent)
                return
            }
            val applicationSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            if (applicationSettingsIntent.resolveActivity(packageManager) != null) {
                startActivity(applicationSettingsIntent)
            }
        } catch (exception: Exception) {
            Log.e(RED_FLAG_TAG, "権限設定画面の起動に失敗しました: $permissionName / ${exception.message}", exception)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RedFlagConfirmationScreen(
    onBack: () -> Unit,
    onOpenExternalUrl: (String) -> Unit,
    permissions: List<RedFlagPermissionInfo>,
    installerInfo: RedFlagInstallerInfo
) {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf(RedFlagLanguage.fromCode(loadRedFlagLanguage(context))) }
    var browserUrl by remember { mutableStateOf<String?>(null) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showIntegrityDialog by remember { mutableStateOf(false) }
    var detailsDisplayedAt by remember { mutableStateOf("") }
    val strings = redFlagStrings(selectedLanguage)
    val packageName = remember { context.packageName }

    MaterialTheme {
        Scaffold(
            containerColor = RED_FLAG_BACKGROUND_COLOR,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.cancel,
                                tint = Color.White
                            )
                        }
                    },
                    title = { Text(text = strings.title, color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = RED_FLAG_HEADER_COLOR,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .padding(bottom = 88.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(text = strings.greeting, color = RED_FLAG_TEXT_COLOR, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (installerInfo.isVerifiedStore) strings.verifiedLine else strings.unverifiedLine,
                        color = RED_FLAG_TEXT_COLOR,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format(Locale.ROOT, strings.runningWithPackage, packageName),
                        color = RED_FLAG_SECONDARY_TEXT_COLOR,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    if (!installerInfo.isVerifiedStore) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showIntegrityDialog = true },
                            shape = RED_FLAG_DIALOG_BUTTON_SHAPE,
                            colors = ButtonDefaults.buttonColors(containerColor = RED_FLAG_M2_PURPLE, contentColor = Color.White)
                        ) { Text(text = strings.warningDetails) }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    RedFlagConfirmationCard(title = strings.about) { browserUrl = RED_FLAG_ABOUT_URL }
                    Spacer(modifier = Modifier.height(8.dp))
                    RedFlagConfirmationCard(title = strings.policies) { browserUrl = RED_FLAG_POLICIES_URL }
                    Spacer(modifier = Modifier.height(8.dp))
                    RedFlagConfirmationCard(title = strings.contact) { browserUrl = RED_FLAG_CONTACT_URL }
                    Spacer(modifier = Modifier.height(24.dp))
                    RedFlagConfirmationCard(
                        title = strings.details,
                        onClick = {
                            detailsDisplayedAt = getRedFlagCurrentDisplayTime()
                            showDetailsDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(text = strings.permissions, color = RED_FLAG_TEXT_COLOR, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    if (permissions.isEmpty()) {
                        Text(text = strings.noPermissions, color = RED_FLAG_SECONDARY_TEXT_COLOR, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    } else {
                        permissions.forEach { permission ->
                            RedFlagPermissionCard(
                                permission = permission,
                                grantedText = strings.permitted,
                                onClick = { openPermissionSettingsFromContext(context, permission.name) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                ExtendedFloatingActionButton(
                    onClick = { showLanguageDialog = true },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).navigationBarsPadding(),
                    shape = RED_FLAG_FAB_SHAPE,
                    containerColor = RED_FLAG_M2_PURPLE,
                    contentColor = Color.White,
                    icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = strings.languageDialogTitle) },
                    text = { Text(text = "Language") }
                )
            }
        }

        if (showDetailsDialog) {
            RedFlagDetailsDialog(
                details = getRedFlagAppDetails(context, packageName, installerInfo),
                displayedAt = detailsDisplayedAt,
                strings = strings,
                onDismiss = { showDetailsDialog = false }
            )
        }
        if (showLanguageDialog) {
            RedFlagLanguageDialog(
                selectedLanguage = selectedLanguage,
                strings = strings,
                onDismiss = { showLanguageDialog = false },
                onSelect = { language ->
                    selectedLanguage = language
                    saveRedFlagLanguage(context, language)
                    showLanguageDialog = false
                }
            )
        }
        if (showIntegrityDialog) {
            RedFlagIntegrityWarningDialog(strings = strings, installerInfo = installerInfo, onDismiss = { showIntegrityDialog = false })
        }
        browserUrl?.let { url ->
            RedFlagExternalBrowserDialog(
                url = url,
                strings = strings,
                onDismiss = { browserUrl = null },
                onConfirm = {
                    browserUrl = null
                    onOpenExternalUrl(url)
                }
            )
        }
    }
}

@Composable
private fun RedFlagConfirmationCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RED_FLAG_SMALL_CORNER_SHAPE,
        colors = CardDefaults.cardColors(containerColor = RED_FLAG_CARD_COLOR),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(text = title, color = RED_FLAG_TEXT_COLOR, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
    }
}

@Composable
private fun RedFlagPermissionCard(permission: RedFlagPermissionInfo, grantedText: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RED_FLAG_SMALL_CORNER_SHAPE,
        colors = CardDefaults.cardColors(containerColor = RED_FLAG_CARD_COLOR),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = permission.label, color = RED_FLAG_TEXT_COLOR, fontSize = 16.sp, modifier = Modifier.weight(1f))
            if (permission.granted) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = grantedText, tint = RED_FLAG_GRANTED_COLOR, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
private fun RedFlagDetailsDialog(details: RedFlagAppDetails, displayedAt: String, strings: RedFlagStrings, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RED_FLAG_SMALL_CORNER_SHAPE,
        containerColor = RED_FLAG_CARD_COLOR,
        title = { Text(text = strings.detailsDialogTitle, color = RED_FLAG_TEXT_COLOR) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                RedFlagDetailRow(strings.systemLanguage, details.language)
                RedFlagDetailRow(strings.environment, details.environment)
                RedFlagDetailRow(strings.versionCode, details.versionCode)
                RedFlagDetailRow(strings.displayTime, displayedAt)
                RedFlagDetailRow(strings.packageName, details.packageName)
                RedFlagDetailRow(strings.installer, details.installer)
                RedFlagDetailRow(strings.safetySystem, details.safetySystemVersion)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                shape = RED_FLAG_DIALOG_BUTTON_SHAPE,
                colors = ButtonDefaults.textButtonColors(contentColor = RED_FLAG_M2_MINT)
            ) { Text(text = strings.close) }
        }
    )
}

@Composable
private fun RedFlagDetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(text = label, color = RED_FLAG_SECONDARY_TEXT_COLOR, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = RED_FLAG_TEXT_COLOR, fontSize = 15.sp)
    }
}

@Composable
private fun RedFlagIntegrityWarningDialog(strings: RedFlagStrings, installerInfo: RedFlagInstallerInfo, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RED_FLAG_SMALL_CORNER_SHAPE,
        containerColor = RED_FLAG_CARD_COLOR,
        title = { Text(text = strings.warningTitle, color = RED_FLAG_TEXT_COLOR) },
        text = {
            Column {
                Text(text = strings.warningText, color = RED_FLAG_TEXT_COLOR, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "${strings.installer}: ${installerInfo.packageName ?: strings.installerUnknown}", color = RED_FLAG_SECONDARY_TEXT_COLOR, fontSize = 13.sp)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                shape = RED_FLAG_DIALOG_BUTTON_SHAPE,
                colors = ButtonDefaults.textButtonColors(contentColor = RED_FLAG_M2_MINT)
            ) { Text(text = strings.close) }
        }
    )
}

@Composable
private fun RedFlagLanguageDialog(selectedLanguage: RedFlagLanguage, strings: RedFlagStrings, onDismiss: () -> Unit, onSelect: (RedFlagLanguage) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RED_FLAG_SMALL_CORNER_SHAPE,
        containerColor = RED_FLAG_CARD_COLOR,
        title = { Text(text = strings.languageDialogTitle, color = RED_FLAG_TEXT_COLOR) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                RedFlagLanguage.entries.forEach { language ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onSelect(language) }.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = language.nativeName, color = RED_FLAG_TEXT_COLOR, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        RadioButton(
                            selected = language == selectedLanguage,
                            onClick = { onSelect(language) },
                            colors = RadioButtonDefaults.colors(selectedColor = RED_FLAG_M2_MINT, unselectedColor = RED_FLAG_SECONDARY_TEXT_COLOR)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                shape = RED_FLAG_DIALOG_BUTTON_SHAPE,
                colors = ButtonDefaults.textButtonColors(contentColor = RED_FLAG_M2_MINT)
            ) { Text(text = strings.close) }
        }
    )
}

@Composable
private fun RedFlagExternalBrowserDialog(url: String, strings: RedFlagStrings, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RED_FLAG_SMALL_CORNER_SHAPE,
        containerColor = RED_FLAG_CARD_COLOR,
        title = { Text(text = strings.browserDialogTitle, color = RED_FLAG_TEXT_COLOR) },
        text = { Text(text = strings.browserDialogText, color = RED_FLAG_TEXT_COLOR) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                shape = RED_FLAG_DIALOG_BUTTON_SHAPE,
                colors = ButtonDefaults.textButtonColors(contentColor = RED_FLAG_M2_MINT)
            ) { Text(text = strings.continueText) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RED_FLAG_DIALOG_BUTTON_SHAPE,
                colors = ButtonDefaults.textButtonColors(contentColor = RED_FLAG_M2_MINT)
            ) { Text(text = strings.cancel) }
        }
    )
}

private fun getRedFlagAppDetails(context: Context, packageName: String, installerInfo: RedFlagInstallerInfo): RedFlagAppDetails {
    val packageManager = context.packageManager
    val language = try {
        val configuration = context.resources.configuration
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) configuration.locales.get(0) else @Suppress("DEPRECATION") configuration.locale
        locale.toLanguageTag()
    } catch (exception: Exception) { "Unknown" }
    val environment = try {
        val configuration = context.resources.configuration
        val deviceType = if (configuration.smallestScreenWidthDp >= 600) "Tablet" else "Smartphone"
        "API ${Build.VERSION.SDK_INT} / $deviceType"
    } catch (exception: Exception) { "Unknown" }
    val versionCode = try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode.toString() else @Suppress("DEPRECATION") packageInfo.versionCode.toString()
        result
    } catch (exception: Exception) { "Unknown" }
    val installerText = if (installerInfo.packageName.isNullOrBlank()) installerInfo.displayName else "${installerInfo.displayName} (${installerInfo.packageName})"
    return RedFlagAppDetails(language = language, environment = environment, versionCode = versionCode, packageName = packageName, installer = installerText, safetySystemVersion = RED_FLAG_VERSION)
}

private fun getRedFlagCurrentDisplayTime(): String {
    return try {
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        formatter.format(Date())
    } catch (exception: Exception) { "Unknown" }
}

private fun loadRedFlagLanguage(context: Context): String {
    return try {
        val preferences = context.getSharedPreferences(RED_FLAG_SETTINGS_NAME, Context.MODE_PRIVATE)
        preferences.getString(RED_FLAG_LANGUAGE_KEY, RedFlagLanguage.JA.code) ?: RedFlagLanguage.JA.code
    } catch (exception: Exception) {
        RedFlagLanguage.JA.code
    }
}

private fun saveRedFlagLanguage(context: Context, language: RedFlagLanguage) {
    try {
        val preferences = context.getSharedPreferences(RED_FLAG_SETTINGS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(RED_FLAG_LANGUAGE_KEY, language.code).apply()
    } catch (exception: Exception) {
        Log.e(RED_FLAG_TAG, "画面言語の保存に失敗しました: ${exception.message}", exception)
    }
}

private fun openPermissionSettingsFromContext(context: Context, permissionName: String) {
    try {
        val packageManager = context.packageManager
        val directPermissionIntent = Intent("android.intent.action.MANAGE_APP_PERMISSIONS").apply {
            putExtra(Intent.EXTRA_PACKAGE_NAME, context.packageName)
        }
        if (directPermissionIntent.resolveActivity(packageManager) != null) {
            context.startActivity(directPermissionIntent)
            return
        }
        val applicationSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
        }
        if (applicationSettingsIntent.resolveActivity(packageManager) != null) {
            context.startActivity(applicationSettingsIntent)
        }
    } catch (exception: Exception) {
        Log.e(RED_FLAG_TAG, "権限設定画面の起動に失敗しました: $permissionName / ${exception.message}", exception)
    }
}
