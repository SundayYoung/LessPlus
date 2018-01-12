package com.felix.lessplus.model.bean

import java.io.Serializable

/**
 * Created by liuhaiyang on 2018/1/11.
 */
class HotMovieResponse : Serializable {
    var start: Int = 0
    var title: String? = null
    var subjects: List<SubjectsBean>? = null


    inner class SubjectsBean : Serializable {

        var rating: RatingBean? = null
        var title: String? = null
        var collect_count: Int = 0
        var original_title: String? = null
        var subtype: String? = null
        var year: String? = null
        var alt: String? = null
        var id: String? = null
        var genres: List<String>? = null

        var images: ImagesBean? = null
        var directors: List<PersonBean>? = null
        var casts: List<PersonBean>? = null


        inner class RatingBean : Serializable {
            var max: Int = 0
            var average: Double = 0.0
            var stars: String? = null
            var min: Int = 0
        }

        inner class ImagesBean : Serializable {
            var small: String? = null
            var large: String? = null
            var medium: String? = null
        }

        inner class PersonBean : Serializable {
            var alt: String? = null
            var type: String? = null
            var name: String? = null
            var id: String? = null
            var avatars: ImagesBean? = null
        }
    }
}