//
//  MuseoVC.swift
//  Eureka_iOS
//
//  Created by Alejandro Chavez Campos on 25/11/20.
//

import UIKit
import WebKit

private let reuseIdentifier = "Cell"

class MuseoVC: UICollectionViewController, WKNavigationDelegate {
    @IBOutlet weak var webMuseo: WKWebView!
    @IBOutlet weak var lblaNombreMuseo: UILabel!
   
    @IBOutlet weak var aCargando: UIActivityIndicatorView!
    var nombreMuseo: String = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        
        webMuseo.navigationDelegate = self
        title = nombreMuseo
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        lblaNombreMuseo.text = nombreMuseo
        cargarPagina(nombreMuseo)
       
    }

    func cargarPagina(_ museo: String){
        

        switch museo {
        case "Museo del Palacio de Bellas Artes":
            
            let direccion = "http://museopalaciodebellasartes.gob.mx/"
            // Los acentos y espacios se deben traducir
            let direccionPorcentaje = direccion.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
            if let url = URL(string: direccionPorcentaje!){
                let request = URLRequest(url: url)
                webMuseo.load(request)
            }

        case "Museo Nacional de Antropología":
            let direccion = "https://www.mna.inah.gob.mx/"
            // Los acentos y espacios se deben traducir
            let direccionPorcentaje = direccion.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
            if let url = URL(string: direccionPorcentaje!){
                let request = URLRequest(url: url)
                webMuseo.load(request)
            }

        case "Museo Soumaya":
            let direccion = "http://www.museosoumaya.org/"
            // Los acentos y espacios se deben traducir
            let direccionPorcentaje = direccion.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
            if let url = URL(string: direccionPorcentaje!){
                let request = URLRequest(url: url)
                webMuseo.load(request)
            }

        default:
            print("Extra case")
        }
        
    }
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        aCargando.stopAnimating()
    }

 
}
