/**
 * DancerApplication
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Location } from './location';


export interface Organizer { 
    readonly id?: string;
    name: string;
    address?: string;
    city?: string;
    country?: string;
    homeLocation?: Location;
    url?: string;
}
