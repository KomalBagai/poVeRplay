using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NewBehaviourScriptbb : MonoBehaviour
{
    private float timer = 0.0f;
    GameObject gameObject;

    void Start()
    {

    }
           
    // Update is called once per frame
    void Update()
    {
        gameObject.GetComponent<Rigidbody>().AddForce(0, 0, 1);
    }
}
