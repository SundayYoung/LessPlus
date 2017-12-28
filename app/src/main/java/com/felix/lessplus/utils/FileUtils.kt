package com.felix.lessplus.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils

import com.felix.lessplus.model.bean.Music
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.*

import java.util.Locale
import java.util.regex.Pattern


/**
 * 文件工具类
 * Created by felix on 2016/1/3.
 */
object FileUtils {
    private val MP3 = ".mp3"
    private val LRC = ".lrc"

    private val appDir: String
        get() = Environment.getExternalStorageDirectory().toString() + "/LessPlus"

    val musicDir: String
        get() {
            val dir = appDir + "/Music/"
            return mkdirs(dir)
        }

    val lrcDir: String
        get() {
            val dir = appDir + "/Lyric/"
            return mkdirs(dir)
        }

    val albumDir: String
        get() {
            val dir = appDir + "/Album/"
            return mkdirs(dir)
        }

    val logDir: String
        get() {
            val dir = appDir + "/Log/"
            return mkdirs(dir)
        }

    val relativeMusicDir: String
        get() {
            val dir = "PonyMusic/Music/"
            return mkdirs(dir)
        }

    fun getSplashDir(context: Context): String {
        val dir = context.filesDir.toString() + "/splash/"
        return mkdirs(dir)
    }

    fun getCorpImagePath(context: Context): String {
        return context.externalCacheDir!!.toString() + "/corp.jpg"
    }

    /**
     * 获取歌词路径<br></br>
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找。
     *
     * @return 如果存在返回路径，否则返回null
     */
    fun getLrcFilePath(music: Music?): String? {
        if (music == null) {
            return null
        }

        var lrcFilePath: String? = lrcDir + getLrcFileName(music.artist, music.title)
        if (!exists(lrcFilePath)) {
            lrcFilePath = music.path!!.replace(MP3, LRC)
            if (!exists(lrcFilePath)) {
                lrcFilePath = null
            }
        }
        return lrcFilePath
    }

    private fun mkdirs(dir: String): String {
        val file = File(dir)
        if (!file.exists()) {
            file.mkdirs()
        }
        return dir
    }

    private fun exists(path: String?): Boolean {
        val file = File(path!!)
        return file.exists()
    }

    fun getMp3FileName(artist: String, title: String): String {
        return getFileName(artist, title) + MP3
    }

    fun getLrcFileName(artist: String?, title: String?): String {
        return getFileName(artist, title) + LRC
    }

    fun getAlbumFileName(artist: String?, title: String?): String {
        return getFileName(artist, title)
    }

    fun getFileName(artist: String?, title: String?): String {
        var artist = artist
        var title = title
        artist = stringFilter(artist)
        title = stringFilter(title)
        if (TextUtils.isEmpty(artist)) {
            //            artist = AppCache.getContext().getString(R.string.unknown);
        }
        if (TextUtils.isEmpty(title)) {
            //            title = AppCache.getContext().getString(R.string.unknown);
        }
        return artist + " - " + title
    }

    fun getArtistAndAlbum(artist: String, album: String): String {
        return if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            ""
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            artist
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            album
        } else {
            artist + " - " + album
        }
    }

    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private fun stringFilter(str: String?): String? {
        if (str == null) {
            return null
        }
        val regEx = "[\\/:*?\"<>|]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").trim { it <= ' ' }
    }

    fun b2mb(b: Int): Float {
        val mb = String.format(Locale.getDefault(), "%.2f", b.toFloat() / 1024f / 1024f)
        return java.lang.Float.valueOf(mb)!!
    }

    fun saveLrcFile(path: String, content: String) {
        try {
            val bw = BufferedWriter(FileWriter(path))
            bw.write(content)
            bw.flush()
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    @Throws(IOException::class)
    fun saveFile(response: ResponseBody, destFileDir: String, destFileName: String): File {
        var mIs: InputStream? = null
        val buf = ByteArray(2048)
        var fos: FileOutputStream? = null
        try {
            mIs = response.byteStream()
            val total = response.contentLength()

            var sum: Long = 0

            val dir = File(destFileDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, destFileName)
            fos = FileOutputStream(file)
            while (true) {
                val len = mIs.read(buf)
                if (len == -1) {
                    break
                }
                fos.write(buf, 0, len)
                sum += len.toLong()
            }
            fos.flush()

            return file

        } finally {
            try {
                response.close()
                if (mIs != null) mIs.close()
            } catch (e: IOException) {
            }

            try {
                if (fos != null) fos.close()
            } catch (e: IOException) {
            }
        }
    }
}
