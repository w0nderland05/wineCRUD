package io.directional.wine.domain

import jakarta.persistence.*


@Entity
data class Wine(
    override var id: Long? = null,
    var type: String,
    var alcohol: Double,
    var acidity: Double,
    var body: String,
    var sweetness: String,
    var tannin: String,
    var servingTemperature: String,
    var score: Int,
    var price: Int,
    var style: String? = null,
    var grade: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importer_id")
    var importer: Importer? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winery_id")
    var winery: Winery,
    @OneToOne(mappedBy = "wine", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var wineAroma: WineAroma? = null,
    @OneToOne(mappedBy = "wine", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var winePairing: WinePairing? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    var region: Region? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grape_id")
    var grape: Grape? = null
) : BaseEntity()

@Entity
data class WineAroma(
    val aroma: String,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    val wine: Wine? = null
) : BaseEntity()

@Entity
data class WinePairing(
    val pairing: String,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    val wine: Wine? = null
) : BaseEntity()


@Entity
data class Winery(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    val region: Region? = null,

    @OneToMany(mappedBy = "winery", fetch = FetchType.LAZY, targetEntity = Wine::class)
    var wines: List<Wine>? = null
) : BaseEntity()

@Entity
data class Region(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_region_id")
    var parentRegion: Region? = null,
    var parentNameKorean: String? = null,
    var parentNameEnglish: String? = null,
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, targetEntity = Grape::class)
    var grapes: List<Grape>? = null,
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, targetEntity = Winery::class)
    var wineries: List<Winery>? = null,
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, targetEntity = Wine::class)
    var wines: List<Wine>? = null
) : BaseEntity()

@Entity
data class Grape(
    val acidity : Int,
    val body : Int,
    val sweetness : Int,
    val tannin : Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    val region: Region?,
    @OneToMany(mappedBy = "grape", fetch = FetchType.LAZY, targetEntity = Wine::class)
    var wines: List<Wine>? = null,
    @OneToOne(mappedBy = "grape", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var share: GrapeShare? = null
) : BaseEntity()

@Entity
data class GrapeShare(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grape_id")
    val grape : Grape,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    val region : Region,
    val share : Double
) : BaseEntity()

@Entity
data class Importer(
    @OneToMany(mappedBy = "importer", fetch = FetchType.LAZY, targetEntity = Wine::class)
    var wines: List<Wine>? = null
) : BaseEntity()

