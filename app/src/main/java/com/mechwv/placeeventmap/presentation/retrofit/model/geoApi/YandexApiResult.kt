package com.mechwv.placeeventmap.presentation.retrofit.model.geoApi
import com.fasterxml.jackson.annotation.*

data class GeocodeResult(
    @JsonProperty("response")
    val response: Response
)

data class Response(
    @JsonProperty("GeoObjectCollection")
    val geoObjectCollection: GeoObjectCollection
)

data class GeoObjectCollection(
    @JsonProperty("featureMember")
    val featureMember: MutableList<FeatureMember> = mutableListOf(),
    @JsonProperty("metaDataProperty")
    val metaDataProperty: MetaDataPropertyX
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class FeatureMember(
    @JsonProperty("GeoObject")
    val geoObject: GeoObject?
)

data class MetaDataPropertyX(
    @JsonProperty("GeocoderResponseMetaData")
    val geocoderResponseMetaData: GeocoderResponseMetaData
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GeoObject(
    @JsonProperty("boundedBy")
    val boundedBy: BoundedBy?=null,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("metaDataProperty")
    val metaDataProperty: MetaDataProperty?=null,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("Point")
    val point: Point?=null
)

data class BoundedBy(
    @JsonProperty("Envelope")
    val envelope: Envelope
)

data class MetaDataProperty(
    @JsonProperty("GeocoderMetaData")
    val geocoderMetaData: GeocoderMetaData
)

data class Point(
    @JsonProperty("pos")
    val pos: String
)

data class Envelope(
    @JsonProperty("lowerCorner")
    val lowerCorner: String,
    @JsonProperty("upperCorner")
    val upperCorner: String
)

data class GeocoderMetaData(
    @JsonProperty("Address")
    val address: Address,
    @JsonProperty("AddressDetails")
    val addressDetails: AddressDetails,
    @JsonProperty("kind")
    val kind: String,
    @JsonProperty("precision")
    val precision: String,
    @JsonProperty("text")
    val text: String
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class Address(
    @JsonProperty("Components")
    val components: List<Component>,
    @JsonProperty("country_code")
    val countryCode: String?=null,
    @JsonProperty("formatted")
    val formatted: String?=null,
    @JsonProperty("postal_code")
    val postalCode: String?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class AddressDetails(
    @JsonProperty("Country")
    val country: Country?=null,
    @JsonProperty("Address")
    val address: String?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class Component(
    @JsonProperty("kind")
    val kind: String,
    @JsonProperty("name")
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class Country(
    @JsonProperty("AddressLine")
    val addressLine: String,
    @JsonProperty("AdministrativeArea")
    val administrativeArea: AdministrativeArea?=null,
    @JsonProperty("CountryName")
    val countryName: String?=null,
    @JsonProperty("CountryNameCode")
    val countryNameCode: String?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class AdministrativeArea(
    @JsonProperty("AdministrativeAreaName")
    val administrativeAreaName: String?=null,
    @JsonProperty("SubAdministrativeArea")
    val subAdministrativeArea: SubAdministrativeArea?=null,
    @JsonProperty("Locality")
    val locality: Locality?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class SubAdministrativeArea(
    @JsonProperty("SubAdministrativeAreaName")
    val subAdministrativeAreaName: String?=null,
    @JsonProperty("Locality")
    val locality: Locality?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class Locality(
    @JsonProperty("LocalityName")
    val localityName: String?=null,
    @JsonProperty("Thoroughfare")
    val thoroughfare: Thoroughfare?=null,
    @JsonProperty("DependentLocality")
    @JsonIgnore
    val dependentLocality: DependentLocality?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class DependentLocality(
    @JsonProperty("DependentLocalityName")
    val dependentLocalityName: String,
    @JsonProperty("DependentLocality")
    @JsonIgnore(true)
    val dependentLocality: DependentLocality?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class Thoroughfare(
    @JsonProperty("Premise")
    val premise: Premise?=null,
    @JsonProperty("ThoroughfareName")
    val thoroughfareName: String?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class Premise(
    @JsonProperty("PostalCode")
    val postalCode: PostalCode?=null,
    @JsonProperty("PremiseNumber")
    val premiseNumber: String?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class PostalCode(
    @JsonProperty("PostalCodeNumber")
    val postalCodeNumber: String?=null
)

@JsonIgnoreProperties(ignoreUnknown=true)
data class GeocoderResponseMetaData(
    @JsonProperty("found")
    val found: String,
    @JsonProperty("request")
    val request: String,
    @JsonProperty("results")
    val results: String,
    @JsonProperty("Point")
    val point: Point?=null,
    @JsonProperty("boundedBy")
    val boundedBy: BoundedBy?=null
)