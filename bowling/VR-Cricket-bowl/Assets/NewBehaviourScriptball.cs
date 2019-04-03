using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NewBehaviourScriptball : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        GameObject gameObject;


    }

    // Update is called once per frame
    void Update()
    {
        gameObject.GetComponent<Rigidbody>().AddForce(0, 0, -700);

    }
}
