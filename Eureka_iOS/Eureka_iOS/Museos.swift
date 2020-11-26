//
//  Museos.swift
//  Eureka_iOS
//
//  Created by Alejandro Chavez Campos on 25/11/20.
//

import UIKit

private let reuseIdentifier = "Cell"

class Museos: UICollectionViewController {
    
    @IBOutlet weak var tabla: UITableView!
    //Arreglos
    private let arrNombresMuseos=["Museo del Palacio de Bellas Artes", "Museo Nacional de Antropología", "Museo Soumaya"]
    private let arrDireccionesMuseos=["Av. Juárez S/N, Centro Histórico de la Ciudad de México, Cuauhtémoc, 06050, CDMX","Av. Paseo de la Reforma s/n, Polanco, Bosque de Chapultepec I Secc, Miguel Hidalgo, 11560 CDMX","Blvd. Miguel de Cervantes Saavedra, Granada, Miguel Hidalgo, 11529 Ciudad de México, CDMX"]
    private let arrImagenesMuseos=["museo_bellas_artes.jpg","museo_de_antropologia.jpg","museo_soumaya.jpg"]
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        title = "Museos"
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let vc = segue.destination as! MuseoVC
        let indice = (tabla.indexPathForSelectedRow?.row)!
        vc.nombreMuseo = arrNombresMuseos[indice]
        
        vc.lblaNombreMuseo.text = arrNombresMuseos[indice]
    }


}

extension Museos:UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrNombresMuseos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let celda = tableView.dequeueReusableCell(withIdentifier: "celdaMuseo", for: indexPath)
        celda.textLabel?.text = arrNombresMuseos[indexPath.row]
        
        celda.detailTextLabel?.text = arrDireccionesMuseos[indexPath.row]
        
        celda.imageView?.image=UIImage(named: arrImagenesMuseos[indexPath.row])
        
        return celda
    }

   
   
}
