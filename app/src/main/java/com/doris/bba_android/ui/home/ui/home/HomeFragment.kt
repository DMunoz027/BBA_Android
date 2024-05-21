package com.doris.bba_android.ui.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.doris.bba_android.databinding.FragmentHomeBinding
import com.doris.bba_android.model.BabyCareModel
import com.doris.bba_android.network.BabyCareManager
import com.doris.bba_android.ui.common.DialogUi
import com.doris.bba_android.ui.home.ui.adapters.BabyCareAdapter
import com.doris.bba_android.ui.home.ui.adapters.BabyCareTimelineAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var _dialog: DialogUi? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //actionButtons()
        loadBabyCareData()
    }

    private fun actionButtons() {

        val babyCareList = mutableListOf(
            BabyCareModel(
                stage = "Recién nacido (0-1 mes).",
                resume = "Suele dormir la mayor parte del tiempo, responde a estímulos básicos como la voz de los padres, y muestra reflejos como el de succión y el de agarre.",
                activities = listOf(
                    "Realiza masajes suaves circulares en la espalda, brazos, piernas y pies del bebé con movimientos delicados",
                    "Proporciona períodos de contacto piel a piel con el bebé, especialmente durante la lactancia materna o mientras lo alimentas con biberón. El contacto piel a piel promueve la regulación térmica, reduce el estrés y fortalece el apego entre el bebé y los padres .",
                    "Habla con el bebé en un tono suave y calmado. Tu voz familiar y reconfortante ayuda a tranquilizar al bebé y estimula su desarrollo auditivo.",
                    "Coloca al bebé en un lugar donde pueda ver las caras de los cuidadores y objetos de colores suaves y contrastantes. Los bebés recién nacidos pueden enfocar objetos cercanos, así que acércalos lentamente para que puedan seguir los movimientos y desarrollar su visión."
                ),
                careTips = listOf(
                    "Usa productos suaves y específicos para bebés para el baño y la limpieza diaria",
                    "Mantén al bebé cómodo y abrigado.",
                    "Cambia los pañales con regularidad.",
                    "Limpia suavemente el área del cordón umbilical siguiendo las indicaciones del pediatra.",
                    "Es esencial brindar mucho amor."
                ),
                feedingTips = listOf(
                    "Establece un horario regular de alimentación, ya sea lactancia materna o alimentación con biberón, según las indicaciones del pediatra",
                    "Vigila las señales de hambre y saciedad del bebé",
                    "Después de cada toma es importante sacar los gases del bebe para evitar malestar abdominal y cólico del lactante (llanto inconsolable y agitación)."
                ),
                sleepTips = listOf(
                    "Ayuda al bebé a distinguir entre el día y la noche manteniendo las luces atenuadas durante la noche y exponiéndolo a la luz natural durante el día.",
                    "Fomenta una rutina de sueño tranquila y consistente.",
                    "Coloca al bebé boca arriba para dormir en un colchón firme y sin almohadas ni juguetes sueltos."
                )
            ),

            BabyCareModel(
                stage = "Bebé pequeño (1-3 meses)",
                resume = "Comienza a mostrar más actividad y alerta, sigue moviendo sus brazos y piernas de manera involuntaria, y puede seguir objetos con la mirada.",
                activities = listOf(
                    "Introduce juguetes de texturas suaves y sonidos suaves para estimular sus sentidos.",
                    "Practica períodos cortos de tiempo boca abajo para fortalecer los músculos del cuello y la espalda.",
                    "Responde a los sonidos y balbuceos del bebé con conversaciones suaves y sonrisas.",
                    "Coloca juguetes coloridos, brillantes y contrastantes dentro de su alcance visual para fomentar la exploración visual.",
                    "Practicar ejercicios suaves como levantar y bajar las piernas al igual que los brazos para fortalecer sus músculos.",
                    "Realiza juegos simples de seguimiento visual con objetos cercanos.",
                    "Practica el juego de &quot;donde está el bebé&quot; (consiste en colocar entre tu cara y la de tu bebé una toalla, o tus propias manos, y acto seguido quitarla diciendo “aquí está”) para fomentar la interacción y la risa.",
                ),
                careTips = listOf(
                    "Continúa con los cuidados básicos de higiene y alimentación.",
                    "Observa y responde a las necesidades del bebé, como el hambre, el sueño y el cambio de pañales."
                ),
                feedingTips = listOf(
                    "Mantén una alimentación regular y adecuada según las recomendaciones del pediatra.",
                    "Observa las señales de satisfacción durante la alimentación."
                ),
                sleepTips = listOf(
                    "Establece una rutina tranquila antes de la hora de dormir, como baños relajantes o canciones suaves.",
                    "Ofrecer al bebé un espacio agradable y seguro para dormir.",
                    "Desde los tres meses puedes hacer que el bebé duerma en su propio espacio, como lo seria una cuna, bajo el cuidado y supervisión de los padres"
                )
            ),

            BabyCareModel(
                stage = "Bebé en crecimiento (4-6 meses)",
                resume = "Puede girarse de espaldas a boca abajo y viceversa, comienza a sentarse con apoyo, muestra interés en los objetos y puede empezar a balbucear.",
                activities = listOf(
                    "Juega a juegos simples como 'donde esta bebé' y 'bravo'; para fomentar la interacción y el desarrollo del lenguaje.",
                    "Promover la estimulación táctil, proporcionando al bebé juguetes con diferentes texturas para que el bebé explore con las manos y la boca.",

                    "Estimula su Desarrollo motor, colocando juguetes atractivos justo fuera del alcance del bebé para alentar el gateo y la exploración.",
                    "Ofrece juguetes que el bebé pueda agarrar y manipular fácilmente.",
                    "Practica el tiempo boca abajo para fortalecer los músculos del cuello y la espalda.",
                    "Anima al bebé a explorar diferentes texturas y sonidos, usa juguetes de tela, pelotas suaves y libros de tela son excelentes opciones.",
                    "ofrecer tiempo boca abajo supervisado para fortalecer los músculos del cuello y la espalda.",
                    "Juega a juegos simples de interacción, como hacer sonidos con la boca o imitar las expresiones faciales del bebé. Esta interacción fomenta el desarrollo del lenguaje y fortalece el vínculo entre tú y tu bebé."
                ),
                careTips = listOf(
                    "Introduce la limpieza bucal con un cepillo extra suave después de las comidas.",
                    "Mantén un entorno seguro y libre de objetos pequeños que el bebé pueda tragar.",
                    "Mantén una rutina regular de cambio de pañales para prevenir la irritación de la piel y la dermatitis del pañal. Usa productos suaves y sin fragancias para limpiar el área del pañal y aplica una crema protectora si es necesario.",
                    "Corta las uñas del bebé regularmente para evitar que se rasque la piel. Utiliza corta uñas para bebé mientras el bebé está relajado.",
                    "Si el bebé ya ha comenzado a desarrollar dientes, limpia suavemente las encías y los dientes con una gasa húmeda después de las comidas. Esto ayuda a prevenir la acumulación de bacterias y promueve una buena salud bucal.",
                    "Si el bebé ya ha comenzado a desarrollar dientes, limpia suavemente las encías y los dientes con una gasa húmeda después de las comidas. Esto ayuda a prevenir la acumulación de bacterias y promueve una buena salud bucal.",
                ),
                feedingTips = listOf(
                    "Introduce alimentos sólidos según el método de alimentación que elijas basado en las indicaciones del pediatra.",
                    "Supervisa las reacciones del bebé a los nuevos alimentos y texturas.",
                    "Acompaña sus comidas con agua preferiblemente.",
                    "Inicio de alimentación complementaria (6 meses) podrás bridarle a tu bebé variedad de frutas y verduras, a excepción de algunas por sus semillas, tamaño, y verduras de hojas verdes.",
                    "Inicialmente no se recomienda la sal hasta su primer año (baja cantidad) y el dulce después de los dos años (lo menos posible o nada).",
                ),
                sleepTips = listOf(
                    "Establece una rutina de sueño regular y consistente.",
                    "Fomenta la independencia para que el bebé aprenda a dormirse solo.",
                )
            ),

            BabyCareModel(
                stage = "Bebé en crecimiento (7-9 meses)",
                resume = "Comienza a gatear o arrastrarse, puede sentarse sin apoyo, empieza a entender palabras simples, y puede mostrar preferencia por ciertos juguetes o actividades. Si el bebé ya ha comenzado a desarrollar dientes, limpia suavemente las encías y los dientes con una gasa húmeda después de las comidas. Esto ayuda a prevenir la acumulación de bacterias y promueve una buena salud bucal.",
                activities = listOf(
                    "Proporciona juguetes que el bebé pueda manipular y explorar, como bloques apilables y juguetes de encaje.",
                    "Lee libros con imágenes simples y señala objetos mientras nombras lo que son para ayudar al bebé a desarrollar su vocabulario.",
                    "Fomenta la exploración sensorial con actividades al aire libre y en interiores.",
                    "Anima al bebé a gatear y explorar diferentes espacios."
                ),
                careTips = listOf(
                    "Asegura las áreas de la casa donde el bebé pueda gatear o explorar para evitar accidentes, y pueda moverse y explorar",
                    "Supervisa activamente al bebé mientras explora para prevenir accidentes.",
                    "Mantén un entorno seguro y libre de peligros."
                ),
                feedingTips = listOf(
                    "Introduce una variedad de alimentos saludables y nutritivos, teniendo en cuenta el método de alimentación y las recomendaciones del pediatra",
                    "Supervisa las habilidades de alimentación del bebé y fomenta la independencia.",
                ),
                sleepTips = listOf(
                    "Mantén una rutina de sueño consistente y relajante.",
                    "Realizar buenas siestas.",
                )
            ),

            BabyCareModel(
                stage = "Bebé en crecimiento (10-12 meses)",
                resume = "Puede estar comenzando a ponerse de pie, dar algunos pasos con apoyo, señalar objetos de interés, y comprender instrucciones simples.",
                activities = listOf(
                    "Ofrece juguetes que el bebé pueda empujar, tirar y montar.",
                    "Anima al bebé a imitar sonidos y gestos simples.",
                    "Proporciona actividades al aire libre para fomentar la exploración y el movimiento.",
                    "Juega a juegos de imitación como tocar instrumentos musicales o imitar sonidos de animales para estimular la creatividad y el aprendizaje social.",
                    "Proporciona juguetes de empuje o montables para ayudar al bebé a desarrollar su equilibrio y coordinación."
                ),
                careTips = listOf(
                    "Continúa con la supervisión activa mientras el bebé se mueve y explora.",
                    "Proporciona un entorno seguro para gatear y caminar, están en una etapa de gran exploración y actividad.",
                    "Asegura los muebles y objetos que el bebé pueda usar para ponerse de pie para prevenir caídas."
                ),
                feedingTips = listOf(
                    "Introduce una variedad de alimentos que fomenten la independencia y la destreza alimentaria..",
                    "Ofrece alimentos que el bebé pueda comer con las manos.",
                ),
                sleepTips = listOf(
                    "Mantén una rutina de sueño tranquila y consistente.",
                    "Fomenta la independencia para que el bebé pueda consolarse y dormirse solo si es necesario.",
                )
            ),
        )
        babyCareList.forEach { babyCare ->
            saveBabyCareData(babyCare)
        }

    }

    private fun saveBabyCareData(babyCare: BabyCareModel) {
        val babyCareManager = BabyCareManager()

        babyCareManager.saveBabyCareData(babyCare,
            onSuccess = {
                Toast.makeText(requireContext(), "Datos guardados exitosamente", Toast.LENGTH_SHORT)
                    .show()
            },
            onFailure = { exception ->
                Toast.makeText(
                    requireContext(),
                    "Error al guardar datos: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            })
    }

    private fun loadBabyCareData() {
        val babyCareManager = BabyCareManager()

        babyCareManager.getBabyCareData(
            onSuccess = { babyCareList ->
                val babyCareAdapter = BabyCareAdapter(babyCareList)
                binding.viewPager.adapter = babyCareAdapter
                binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            },
            onFailure = { exception ->
                Toast.makeText(
                    requireContext(),
                    "Error al cargar datos: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}